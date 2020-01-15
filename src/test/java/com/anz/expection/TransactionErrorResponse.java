package com.anz.expection;

import org.springframework.web.reactive.function.client.WebClientResponseException;

public class TransactionErrorResponse extends Exception {

    public TransactionErrorResponse(String statusText, WebClientResponseException cause) {
        super(statusText, cause);
    }

    public TransactionErrorResponse(Exception cause) {
        super(cause);
    }


}
