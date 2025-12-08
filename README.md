# How to run locally
1. Make sure you have Java 21 and Maven installed.
2. Compile the project using Maven: `mvn clean install`
3. Run the application: `java -jar target/Tiny-Ledger-0.0.1-SNAPSHOT.jar`
4. The application will start on port 8080.

# API Endpoints

All endpoints are available under the `/accounts` path.

## Create Account
Creates a new account.
- **POST** `/accounts`
- **Request Body:**
```json
{
  "fullName": "John Doe",
  "dateOfBirth": "1990-01-01",
  "address": "123 Main St"
}
```
- **Response:**
```json
{
  "accountNumber": 123456789,
  "fullName": "John Doe",
  "dateOfBirth": "1990-01-01",
  "address": "123 Main St"
}
```

## Get Account Info
Retrieves basic information for a given account.
- **GET** `/accounts/{accountNumber}`
- **Response:**
```json
{
  "accountNumber": 123456789,
  "fullName": "John Doe",
  "dateOfBirth": "1990-01-01",
  "address": "123 Main St"
}
```

## Get Account Balance
Retrieves the balance for a given account.
- **GET** `/accounts/{accountNumber}/balance`
- **Response:**
```json
{
  "accountNumber": 123456789,
  "fullName": "John Doe",
  "balance": 100.00
}
```

## Deposit
Deposits a given amount into an account.
- **POST** `/accounts/{accountNumber}/deposit`
- **Request Body:**
```json
{
  "amount": 50.00
}
```

## Withdraw
Withdraws a given amount from an account.
- **POST** `/accounts/{accountNumber}/withdraw`
- **Request Body:**
```json
{
  "amount": 20.00
}
```

## Get Account Transactions
Retrieves the list of transactions for a given account (ordered by latest).
- **GET** `/accounts/{accountNumber}/transactions`
- **Response:**
```json
[
  {
    "accountNumber": 123456789,
    "timestamp": 1672531200,
    "operation": "DEPOSIT",
    "amount": 100.00
  },
  {
    "accountNumber": 123456789,
    "timestamp": 1672534800,
    "operation": "WITHDRAW",
    "amount": 20.00
  }
]
