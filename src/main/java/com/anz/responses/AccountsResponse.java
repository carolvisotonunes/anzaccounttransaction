package com.anz.responses;

import com.anz.model.Account;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class AccountsResponse {
    private List<Account> accounts;

    @JsonCreator
    public AccountsResponse(@JsonProperty("accounts") List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountsResponse that = (AccountsResponse) o;
        return Objects.equals(accounts, that.accounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accounts);
    }

    @Override
    public String toString() {
        return "AccountsResponse{" +
                "accounts=" + accounts +
                '}';
    }
}
