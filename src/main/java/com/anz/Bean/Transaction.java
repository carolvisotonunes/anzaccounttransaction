package com.anz.Bean;

public class Transaction {


    private long transactionId;
    private Account account;
    private String accountName;
    private String valueDate;
    private String currency;
    private double debitAmount;
    private double creditAmount;
    private String transactionType;
    private String description;


    public Transaction(long transactionId, Account account, String accountName, String valueDate, String currency, double debitAmount, double creditAmount, String transactionType, String description) {
        this.transactionId = transactionId;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getValueDate() {
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
                ", account=" + account +
                ", accountName='" + accountName + '\'' +
                ", valueDate='" + valueDate + '\'' +
                ", currency='" + currency + '\'' +
                ", debitAmount=" + debitAmount +
                ", creditAmount=" + creditAmount +
                ", transactionType='" + transactionType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
