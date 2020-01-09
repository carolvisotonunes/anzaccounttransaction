package com.anz.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

public class Transaction {

    private final long transactionId;
    private final long accountId;
    private final String accountName;
    private final LocalDate valueDate;
    private final String currency;
    private final double debitAmount;
    private final double creditAmount;
    private final String transactionType;
    private final String description;

    @JsonCreator
    public Transaction(@JsonProperty("transactionId")long transactionId,
                       @JsonProperty("accountId")long accountId,
                       @JsonProperty("accountName")String accountName,
                       @JsonProperty("valueDate")LocalDate valueDate,
                       @JsonProperty("currency")String currency,
                       @JsonProperty("debitAmount")double debitAmount,
                       @JsonProperty("creditAmount")double creditAmount,
                       @JsonProperty("transactionType")String transactionType,
                       @JsonProperty("description")String description) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionId == that.transactionId &&
                accountId == that.accountId &&
                Double.compare(that.debitAmount, debitAmount) == 0 &&
                Double.compare(that.creditAmount, creditAmount) == 0 &&
                Objects.equals(accountName, that.accountName) &&
                Objects.equals(valueDate, that.valueDate) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(transactionType, that.transactionType) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, accountId, accountName, valueDate, currency, debitAmount, creditAmount, transactionType, description);
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
