package com.t.tiny.ledger.controller;

import com.t.tiny.ledger.controller.dto.AccountBaseInfoDTO;
import com.t.tiny.ledger.controller.dto.TransactionDTO;
import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.controller.request.DepositRequest;
import com.t.tiny.ledger.controller.request.WithdrawRequest;
import com.t.tiny.ledger.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void validateAccountLifecycle() throws Exception {
        // 1. Create Account
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(
                "John Doe",
                LocalDate.of(1992, 10, 27),
                "Street ABC N 90"
        );

        MvcResult createResult = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").exists())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.dateOfBirth").value("1992-10-27"))
                .andExpect(jsonPath("$.address").value("Street ABC N 90"))
                .andReturn();

        AccountBaseInfoDTO createdAccount = objectMapper.readValue(createResult.getResponse().getContentAsString(), AccountBaseInfoDTO.class);
        long accountNumber = createdAccount.accountNumber();

        // 2. Get and validate basic account info
        mockMvc.perform(get("/accounts/{accountNumber}", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.dateOfBirth").value("1992-10-27"))
                .andExpect(jsonPath("$.address").value("Street ABC N 90"));

        // 3. Get and validate account balance
        mockMvc.perform(get("/accounts/{accountNumber}/balance", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.balance").value("0.0"));

        // 4. Deposit money
        DepositRequest depositRequest = new DepositRequest(new BigDecimal("100.00"));
        mockMvc.perform(post("/accounts/{accountNumber}/deposit", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk());

        // 5. Withdraw money
        WithdrawRequest withdrawRequest = new WithdrawRequest(new BigDecimal("50.00"));
        mockMvc.perform(post("/accounts/{accountNumber}/withdraw", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isOk());

        // 6. Get transactions
        MvcResult transactionsResult = mockMvc.perform(get("/accounts/{accountNumber}/transactions", accountNumber))
                .andExpect(status().isOk())
                .andReturn();
        List<TransactionDTO> transactions = objectMapper.readValue(transactionsResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(2, transactions.size());
        assertEquals(Transaction.Operation.WITHDRAW, transactions.get(0).operation());
        assertEquals(Transaction.Operation.DEPOSIT, transactions.get(1).operation());

        // 7. Check final account balance
        mockMvc.perform(get("/accounts/{accountNumber}/balance", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(50.00));
    }

    @Test
    void validateAccountControllerErrorHandling() throws Exception {
        mockMvc.perform(get("/accounts/{accountNumber}", 123L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Account number 123 not found"));
    }
}