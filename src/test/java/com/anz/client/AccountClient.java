package com.anz.client;

import com.anz.expection.AccountErrorResponse;
import com.anz.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public class AccountClient {
    Logger  log =  LoggerFactory.getLogger(AccountClient.class);
    public static final String ACCOUNT_BY_ID_PATH_PARAM = "/accounts/{accountID}";
    public static final String ADD_ACCOUNT = "/accounts/";
    public static final String UPDATE_ACCOUNT = "/accounts/";
    public static final String ACCOUNT_DELETED_BY_ID_PATH_PARAM = "/accounts/{accountID}";
    private WebClient webClient;

    public AccountClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Account retrieveAccountById(Integer accountId) throws AccountErrorResponse {
        try {
            return webClient.get().uri(ACCOUNT_BY_ID_PATH_PARAM, accountId) //mapping the movie id to the url
                    .retrieve()
                    .bodyToMono(Account.class) //body is converted to Mono(Represents single item)
                    .block();
        }catch (WebClientResponseException ex) {
            log.error("WebClientResponseException - Error Message is : {} ", ex, ex.getResponseBodyAsString());
            throw new AccountErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) {
            log.error("Exception - The Error Message is {} ", ex.getMessage());
            throw new AccountErrorResponse(ex);
        }
    }


    public Account addNewAccount(Account newAccount) throws AccountErrorResponse {
        Account movie;

        try {
            movie = webClient.post().uri(ADD_ACCOUNT)
                    .syncBody(newAccount)
                    .retrieve()
                    .bodyToMono(Account.class)
                    .block();
            log.info("New Account SuccessFully addded {} ", movie);
        } catch (WebClientResponseException ex) {
            log.error("WebClientResponseException - Error Message is : {} , and the Error Response Body is {}", ex, ex.getResponseBodyAsString());
            throw new AccountErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) {
            log.error("Exception - The Error Message is {} ", ex.getMessage());
            throw new AccountErrorResponse(ex);
        }
        return movie;
    }

    public HttpStatus updateAccount( Account account) throws AccountErrorResponse {
        try {
            return webClient.put().uri(UPDATE_ACCOUNT, account.getAccountId())
                    .syncBody(account)
                    .exchange()
                    .block()
                    .statusCode();
        } catch (WebClientResponseException ex) {
            log.error("WebClientResponseException - Error Message is : {}", ex, ex.getResponseBodyAsString());
            throw new AccountErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) {
            log.error("Exception - The Error Message is {} ", ex.getMessage());
            throw new AccountErrorResponse(ex);
        }
    }

    public HttpStatus deleteAccountById(long accountId) throws AccountErrorResponse {

        HttpStatus response;
        try {
            response = webClient.delete().uri(ACCOUNT_DELETED_BY_ID_PATH_PARAM, accountId)
                    .exchange()
                    .block()
                    .statusCode();
        }catch (WebClientResponseException ex) {
            log.error("WebClientResponseException - Error Message is : {}", ex, ex.getResponseBodyAsString());
            throw new AccountErrorResponse(ex.getStatusText(), ex);
        } catch (Exception ex) {
            log.error("Exception - The Error Message is {} ", ex.getMessage());
            throw new AccountErrorResponse(ex);
        }
        return response;

    }

}
