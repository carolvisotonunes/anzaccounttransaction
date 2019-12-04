package com.anz.model;

import java.sql.Date;

public class Account {

    private final long accountId;
    private final long customerId;
    private final String accountName;
    private final String accountType;
    private final Date balanceDate;
    private final String currency;
    private final double availableBalance;

    public Account(long accountNumber,
                   long customerId,
                   String accountName,
                   String accountType,
                   Date balanceDate,
                   String currency,
                   double availableBalance) {
        this.accountId = accountNumber;
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

    public Date getBalanceDate() {
        return balanceDate;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountId +
                ", customerId=" + customerId +
                ", accountName='" + accountName + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balanceDate='" + balanceDate + '\'' +
                ", currency=" + currency +
                ", availableBalance=" + availableBalance +
                '}';
    }
}
