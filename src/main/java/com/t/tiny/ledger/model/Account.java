package com.t.tiny.ledger.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Account {

    private final long accountNumber;
    private final String fullName;
    private final LocalDate dateOfBirth;
    private final String address;

    private BigDecimal balance;

    public Account(long accountNumber, String fullName,
                   LocalDate dateOfBirth, String address) {
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.balance = BigDecimal.ZERO;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
