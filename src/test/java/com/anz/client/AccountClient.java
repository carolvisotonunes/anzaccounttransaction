package com.anz.client;

import com.anz.controller.TestResponse;
import com.anz.model.Account;
import com.anz.responses.AccountsResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
public class AccountClient {
    private static final String ALL_ACCOUNT = "/accounts/";
    private static final String ACCOUNT_BY_ID_PATH_PARAM = "/accounts/{accountID}";
    private static final String ADD_ACCOUNT = "/accounts/";
    private static final String UPDATE_ACCOUNT = "/accounts/";
    private static final String ACCOUNT_DELETED_BY_ID_PATH_PARAM = "/accounts/{accountID}";
    private Logger log = LoggerFactory.getLogger(AccountClient.class);
    private WebClient webClient;

    public AccountClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public TestResponse< List<Account>> retrieveAllAccounts() {
        ClientResponse clientResponse = webClient
                .get()
                .uri(ALL_ACCOUNT)
                .exchange()
                .block();
        return new TestResponse<>(
                clientResponse.statusCode(),
                clientResponse
                        .bodyToMono(AccountsResponse.class)
                        .block()
                        .getAccounts()
        );
    }

    public TestResponse <Account> retrieveAccountById(Integer accountId) {
        ClientResponse response = webClient
                .get()
                .uri(ACCOUNT_BY_ID_PATH_PARAM, accountId)
                .exchange()
                .block();
        return new TestResponse<>(
                response.statusCode(),
                response.bodyToMono(Account.class).block()
        );
    }

    public TestResponse<Account> addNewAccount(Account newAccount) {
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
    }

    public HttpStatus updateAccount(Account account) {
        return webClient
                .put()
                .uri(UPDATE_ACCOUNT, account.getAccountId())
                .bodyValue(account)
                .exchange()
                .block()
                .statusCode();
    }

    public HttpStatus deleteAccountById(long accountId) {
        return webClient
                .delete()
                .uri(ACCOUNT_DELETED_BY_ID_PATH_PARAM, accountId)
                .exchange()
                .block()
                .statusCode();
    }
}
