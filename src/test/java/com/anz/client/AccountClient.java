package com.anz.client;

import com.anz.controller.TestResponse;
import com.anz.expection.AccountErrorResponse;
import com.anz.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public class AccountClient {
    private static final String ACCOUNT_BY_ID_PATH_PARAM = "/accounts/{accountID}";
    private static final String ADD_ACCOUNT = "/accounts/";
    private static final String UPDATE_ACCOUNT = "/accounts/";
    private static final String ACCOUNT_DELETED_BY_ID_PATH_PARAM = "/accounts/{accountID}";
    private Logger log = LoggerFactory.getLogger(AccountClient.class);
    private WebClient webClient;

    public AccountClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Account retrieveAccountById(Integer accountId) throws AccountErrorResponse {
        try {
            return webClient
                    .get()
                    .uri(ACCOUNT_BY_ID_PATH_PARAM, accountId)
                    .retrieve()
                    .bodyToMono(Account.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("WebClientResponseException - Error Message is : {} {}", ex, ex.getResponseBodyAsString());
            throw new AccountErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) {
            log.error("Exception - The Error Message is {} ", ex.getMessage());
            throw new AccountErrorResponse(ex);
        }
    }

    public TestResponse<Account> addNewAccount(Account newAccount) throws AccountErrorResponse {
        try {
            ClientResponse response = webClient
                    .post()
                    .uri(ADD_ACCOUNT)
                    .bodyValue(newAccount)
                    .exchange()
                    .block();
            return new TestResponse<>(
                    response.statusCode(),
                    response.bodyToMono(Account.class).block()
            );
        } catch (WebClientResponseException ex) {
            log.error("WebClientResponseException - Error Message is : {} , and the Error Response Body is {}", ex, ex.getResponseBodyAsString());
            throw new AccountErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) {
            log.error("Exception - The Error Message is {} ", ex.getMessage());
            throw new AccountErrorResponse(ex);
        }
    }

    public HttpStatus updateAccount(Account account) throws AccountErrorResponse {
        try {
            return webClient
                    .put()
                    .uri(UPDATE_ACCOUNT, account.getAccountId())
                    .bodyValue(account)
                    .exchange()
                    .block()
                    .statusCode();
        } catch (WebClientResponseException ex) {
            log.error("WebClientResponseException - Error Message is : {} {}", ex, ex.getResponseBodyAsString());
            throw new AccountErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) {
            log.error("Exception - The Error Message is {} ", ex.getMessage());
            throw new AccountErrorResponse(ex);
        }
    }

    public HttpStatus deleteAccountById(long accountId) throws AccountErrorResponse {
        try {
            HttpStatus response = webClient
                    .delete()
                    .uri(ACCOUNT_DELETED_BY_ID_PATH_PARAM, accountId)
                    .exchange()
                    .block()
                    .statusCode();
            return response;
        } catch (WebClientResponseException ex) {
            log.error("WebClientResponseException - Error Message is : {} {}", ex, ex.getResponseBodyAsString());
            throw new AccountErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) {
            log.error("Exception - The Error Message is {} ", ex.getMessage());
            throw new AccountErrorResponse(ex);
        }
    }
}
