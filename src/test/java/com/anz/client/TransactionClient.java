package com.anz.client;

import com.anz.controller.TestResponse;
import com.anz.expection.TransactionErrorResponse;
import com.anz.model.Account;
import com.anz.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class TransactionClient {

    private static final String TRANSACTION_BY_ID_PATH_PARAM = "/transactions/{transactionId}";
    private static final String ADD_TRANSACTION = "/transactions/";
    private static final String UPDATE_TRANSACTION = "/transactions/";
    private static final String TRANSACTION_DELETED_BY_ID_PATH_PARAM = "/transactions/{transactionId}";
    private Logger log = LoggerFactory.getLogger(TransactionClient.class);
    private WebClient webClient;

    public TransactionClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Transaction retrieveTransactionById(Long transactionId) throws TransactionErrorResponse {
        try {
            return webClient.get().uri(TRANSACTION_BY_ID_PATH_PARAM, transactionId) //mapping the movie id to the url
                    .retrieve()
                    .bodyToMono(Transaction.class) //body is converted to Mono(Represents single item)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("WebClientResponseException - Error Message is : {} ", ex, ex.getResponseBodyAsString());
            throw new TransactionErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) {
            log.error("Exception - The Error Message is {} ", ex.getMessage());
            throw new TransactionErrorResponse(ex);
        }
    }


    public TestResponse<Transaction> addNewTransaction(Transaction newTransaction) throws TransactionErrorResponse {
        try {
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
        } catch (WebClientResponseException ex) {
            log.error("WebClientResponseException - Error Message is : {} , and the Error Response Body is {}", ex, ex.getResponseBodyAsString());
            throw new TransactionErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) {
            log.error("Exception - The Error Message is {} ", ex.getMessage());
            throw new TransactionErrorResponse(ex);
        }
    }

    public HttpStatus updateTransaction(Transaction transaction) throws TransactionErrorResponse {
        try {
            return webClient.
                    put()
                    .uri(UPDATE_TRANSACTION, transaction.getTransactionId())
                    .bodyValue(transaction)
                    .exchange()
                    .block()
                    .statusCode();
        } catch (WebClientResponseException ex) {
            log.error("WebClientResponseException - Error Message is : {}", ex, ex.getResponseBodyAsString());
            throw new TransactionErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) {
            log.error("Exception - The Error Message is {} ", ex.getMessage());
            throw new TransactionErrorResponse(ex);
        }
    }

    public HttpStatus deleteTransactionById(long transactionId) throws TransactionErrorResponse {

        HttpStatus response;
        try {
            response = webClient.delete().uri(TRANSACTION_DELETED_BY_ID_PATH_PARAM, transactionId)
                    .exchange()
                    .block()
                    .statusCode();
        } catch (WebClientResponseException ex) {
            log.error("WebClientResponseException - Error Message is : {}", ex, ex.getResponseBodyAsString());
            throw new TransactionErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) {
            log.error("Exception - The Error Message is {} ", ex.getMessage());
            throw new TransactionErrorResponse(ex);
        }
        return response;

    }

}
