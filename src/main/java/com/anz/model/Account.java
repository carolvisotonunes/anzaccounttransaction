package com.anz.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

public class Account {

    private final long accountId;
    private final long customerId;
    private final String accountName;
    private final String accountType;
    private final LocalDate balanceDate;
    private final String currency;
    private final double availableBalance;

    @JsonCreator
    public Account(@JsonProperty("accountId") long accountId,
                   @JsonProperty("customerId") long customerId,
                   @JsonProperty("accountName") String accountName,
                   @JsonProperty("accountType") String accountType,
                   @JsonProperty("balanceDate") LocalDate balanceDate,
                   @JsonProperty("currency") String currency,
                   @JsonProperty("availableBalance") double availableBalance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountName = accountName;
        this.accountType = accountType;
        this.balanceDate = balanceDate;
        this.currency = currency;
        this.availableBalance = availableBalance;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public LocalDate getBalanceDate() {
        return balanceDate;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    // TODO: Review equals and hashcode of Java
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountId == account.accountId &&
                customerId == account.customerId &&
                Double.compare(account.availableBalance, availableBalance) == 0 &&
                Objects.equals(accountName, account.accountName) &&
                Objects.equals(accountType, account.accountType) &&
                Objects.equals(balanceDate, account.balanceDate) &&
                Objects.equals(currency, account.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, customerId, accountName, accountType, balanceDate, currency, availableBalance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", customerId=" + customerId +
                ", accountName='" + accountName + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balanceDate='" + balanceDate + '\'' +
                ", currency=" + currency +
                ", availableBalance=" + availableBalance +
                '}';
    }
}
