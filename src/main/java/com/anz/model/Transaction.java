package com.anz.model;

import java.sql.Date;
import java.time.LocalDate;


// FIXME: Put JSON annotations
public class Transaction {

    private final long transactionId;
    private final long accountId;
    private final String accountName;
    private final LocalDate valueDate; // FIXME: transform this into a localdate
    private final String currency;
    private final double debitAmount;
    private final double creditAmount;
    private final String transactionType;
    private final String description;

    public Transaction(long transactionId, long accountId, String accountName, LocalDate valueDate, String currency, double debitAmount, double creditAmount, String transactionType, String description) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.accountName = accountName;
        this.valueDate = valueDate;
        this.currency = currency;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.transactionType = transactionType;
        this.description = description;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public String getCurrency() {
        return currency;
    }

    public double getDebitAmount() {
        return debitAmount;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", valueDate=" + valueDate +
                ", currency='" + currency + '\'' +
                ", debitAmount=" + debitAmount +
                ", creditAmount=" + creditAmount +
                ", transactionType='" + transactionType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
