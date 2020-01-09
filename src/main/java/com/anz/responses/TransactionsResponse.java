package com.anz.responses;

import com.anz.model.Transaction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class TransactionsResponse {

    private List<Transaction> transactions;
    @JsonCreator
    public TransactionsResponse(@JsonProperty("transactions")List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionsResponse that = (TransactionsResponse) o;
        return Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactions);
    }

    @Override
    public String toString() {
        return "TransactionsResponse{" +
                "transactions=" + transactions +
                '}';
    }
}
