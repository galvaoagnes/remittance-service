package com.inter.remittance.domain.commom;

public class ErrorCatalog {
    public static final String PERSON_NOT_FOUND = "Person not found";
    public static final String INVALID_CPF_NUMBER = "Invalid CPF number";
    public static final String INVALID_CNPJ_NUMBER = "Invalid CNPJ number";
    public static final String INVALID_LAST_NAME = "Invalid last name";
    public static final String INVALID_NAME = "Invalid last name";
    public static final String INVALID_EMAIL= "Invalid email";
    public static final String INVALID_TYPE = "Person type cannot be null";
    public static final String INVALID_DOCUMENT = "Document cannot be null";
    public static final String INVALID_DAILY_TRANSACTION_LIMIT = "Invalid daily transaction limit";
    public static final String INVALID_ACCOUNT_PERSON = "Invalid account person";
    public static final String INVALID_ACCOUNT_WALLET = "wallet cannot be null";
    public static final String INVALID_CURRENCY_WALLET = "Wallet with this currency already exists ";
    public static final String INVALID_REMITTANCE_ORIGIN_WALLET_ID = "Origin wallet id cannot be null";
    public static final String INVALID_REMITTANCE_DESTINATION_WALLET_ID = "Destination wallet id cannot be null";
    public static final String INVALID_REMITTANCE_STATUS = "Remittance status cannot be null";
    public static final String INVALID_ORIGINAL_CURRENCY_AMOUNT = "Original currency balance cannot be null or negative";
    public static final String INVALID_EXCHANGE_RATE = "Exchange rate cannot be null and must be greater than zero";
    public static final String INVALID_CONVERTED_AMOUNT = "Converted balance cannot be null or negative";
    public static final String INVALID_CREATED_AT = "Created date cannot be null";
    public static final String INVALID_UPDATED_AT = "Updated date cannot be null";
    public static final String ACCOUNT_NOT_FOUND = "Account not found";
    public static final String WALLET_NOT_FOUND = "Wallet not found";
    public static final String TRANSACTION_NOT_FOUND = "Transaction not found";
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds";
    public static final String INVALID_CURRENCY = "Invalid currency";
    public static final String INVALID_AMOUNT = "Invalid balance";
    public static final String DOCUMENT_ALREADY_EXISTS = "Document already exists";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String INVALID_ACCOUNT_WALLETS = "";
    public static final String DAILY_LIMIT_EXCEEDED = "Daily limit exceeded";
    public static final String REMITTANCE_NOT_FOUND = "Remittance not found";
    public static final String INVALID_PASSWORD = "invalid password" ;
}
