package com.anz.controller;

import org.springframework.http.HttpStatus;

public class TestResponse<T> {
    private HttpStatus status;
    private T body;

    public TestResponse(HttpStatus status, T body) {
        this.status = status;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public T getBody() {
        return body;
    }
}
