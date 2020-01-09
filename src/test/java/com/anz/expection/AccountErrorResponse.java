package com.anz.expection;

import org.springframework.web.reactive.function.client.WebClientResponseException;

public class AccountErrorResponse extends Throwable {
    public AccountErrorResponse(String statusText, WebClientResponseException cause) {
        super(statusText, cause);
    }

    public AccountErrorResponse(Exception cause) {
        super(cause);
    }
}
