package com.anz.Bean;

public class Account {

    private long accountNumber;
    private long customerId;
    private String accountName;
    private String accountType;
    private String balanceDate;
    private double currency;
    private double availableBalance;


    public Account(long accountNumber, long customerId, String accountName, String accountType, String balanceDate, double currency, double availableBalance) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountName = accountName;
        this.accountType = accountType;
        this.balanceDate = balanceDate;
        this.currency = currency;
        this.availableBalance = availableBalance;
    }

    public long getAccountNumber() {
        return accountNumber;
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

    public String getBalanceDate() {
        return balanceDate;
    }

    public double getCurrency() {
        return currency;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", customerId=" + customerId +
                ", accountName='" + accountName + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balanceDate='" + balanceDate + '\'' +
                ", currency=" + currency +
                ", availableBalance=" + availableBalance +
                '}';
    }
}
