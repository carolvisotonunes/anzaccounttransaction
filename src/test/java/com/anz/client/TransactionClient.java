package com.anz.client;

import com.anz.controller.TestResponse;
import com.anz.model.Account;
import com.anz.model.Transaction;
import com.anz.responses.TransactionsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class TransactionClient {
    private static final String ALL_TRANSACTIONS = "/transactions";
    private static final String TRANSACTION_BY_ID_PATH_PARAM = "/transactions/{transactionId}";
    private static final String ADD_TRANSACTION = "/transactions/";
    private static final String UPDATE_TRANSACTION = "/transactions/";
    private static final String TRANSACTION_DELETED_BY_ID_PATH_PARAM = "/transactions/{transactionId}";
    private Logger log = LoggerFactory.getLogger(TransactionClient.class);
    private WebClient webClient;

    public TransactionClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public TestResponse<List<Transaction>> retrieveAllTransaction() {
        ClientResponse clientResponse = webClient
                .get()
                .uri(ALL_TRANSACTIONS)
                .exchange()
                .block();
        return new TestResponse<>(
                clientResponse.statusCode(),
                clientResponse.
                        bodyToMono(TransactionsResponse.class)
                .block()
                .getTransactions()
        );
    }

    public TestResponse<Transaction> retrieveTransactionById(Long transactionId) {
        ClientResponse response = webClient
                .get()
                .uri(TRANSACTION_BY_ID_PATH_PARAM, transactionId)
                .exchange()
                .block();
        return new TestResponse<>(
                response.statusCode(),
                response.bodyToMono(Transaction.class)
                        .block()
        );
    }

    public TestResponse<Transaction> addNewTransaction(Transaction newTransaction) {
        ClientResponse response = webClient
                .post()
                .uri(ADD_TRANSACTION)
                .bodyValue(newTransaction)
                .exchange()
                .block();
        return new TestResponse<>(
                response.statusCode(),
                response.bodyToMono(Transaction.class)
                        .block()
        );
    }

    public HttpStatus updateTransaction(Transaction transaction) {
        return webClient.
                put()
                .uri(UPDATE_TRANSACTION, transaction.getTransactionId())
                .bodyValue(transaction)
                .exchange()
                .block()
                .statusCode();
    }

    public HttpStatus deleteTransactionById(long transactionId) {
        return webClient.delete().uri(TRANSACTION_DELETED_BY_ID_PATH_PARAM, transactionId)
                .exchange()
                .block()
                .statusCode();
    }
}
