package com.anz.controller;

import com.anz.DbHelper;
import com.anz.client.AccountClient;
import com.anz.dao.AccountDAO;
import com.anz.expection.AccountErrorResponse;
import com.anz.model.Account;
import com.anz.responses.AccountsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class AccountControllerTest {
    private static final String ACCOUNTS_URL = "http://localhost:8080/accounts";

    private RestTemplate restTemplate;
    private AccountDAO accountDAO;
    private WebClient webClient;
    AccountClient accountClient;

    @BeforeEach
    public void setUp() throws Exception {
        int port = 8080;
        final String baseUrl = String.format("http://localhost:%s/", port);
        webClient = WebClient.create(baseUrl);
        accountClient = new AccountClient(webClient);
        restTemplate = new RestTemplate();
        accountDAO = new AccountDAO();
        DbHelper dbHelper = new DbHelper();
        dbHelper.truncate();
    }

    @Test
    public void returnsAccountsWhenRequestSucceeds() throws URISyntaxException, SQLException {
        // Given
        List<Account> accounts = Arrays.asList(
                new Account(1, 78541236, "Mark", "SAVINGS", LocalDate.of(2019, 7, 1), "AUD", 0),
                new Account(2, 78541236, "Tim", "SAVINGS", LocalDate.of(2019, 7, 2), "USD", 0),
                new Account(3, 88542169, "Mary", "SAVINGS", LocalDate.of(2019, 7, 3), "EUR", 0)
        );
        for (Account account : accounts) {
            accountDAO.insert(account);
        }
        // When
        ResponseEntity<AccountsResponse> result = restTemplate.getForEntity(new URI(ACCOUNTS_URL), AccountsResponse.class);
        // Then
        assertThat(result.getStatusCodeValue(), equalTo(200));
        assertThat(result.getBody().getAccounts(), containsInAnyOrder(accounts.toArray(new Account[0])));
    }

    @Test
    public void retrieveAccountById() throws SQLException, AccountErrorResponse {
        // Given
        Account expectedAccount = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 7, 1), "AUD", 0);
        accountDAO.insert(expectedAccount);

        // When
        Account account = accountClient.retrieveAccountById(1);

        // Then
        assertThat(account, equalTo(expectedAccount));
    }

    @Test
    public void retrieveAccountById_NotFound() throws SQLException {
        // Given
        Account expectedAccount = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 7, 1), "AUD", 0);
        accountDAO.insert(expectedAccount);

        assertThrows(AccountErrorResponse.class, () -> accountClient.retrieveAccountById(100));
    }


    @Test
    void addNewAccount() throws SQLException, AccountErrorResponse {
        //given
        Account expectedAccount = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 7, 1), "AUD", 0);
        //when
        Account account = accountClient.addNewAccount(new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 7, 1), "AUD", 0));
        //then
        assertThat(account, equalTo(expectedAccount));

    }

    @Test
    void addNewAccount_InvalidInput() {
        //when
        Assertions.assertThrows(AccountErrorResponse.class, () -> accountClient.addNewAccount(null));
    }

    @Test
    void updateAccount() throws SQLException, AccountErrorResponse {
        //given
       Account insert = new Account(2, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 12, 1), "AUD", 1000);
        accountDAO.insert(insert);

        Account toBeUpdate = new Account(2, 78541236, "Mark", "SAVINGS",
                LocalDate.now(), "AUD", 0);
        //when
        HttpStatus status = accountClient.updateAccount(toBeUpdate);
        //then
        assertThat(status, equalTo(HttpStatus.OK));

    }

    @Test
    void updateAccount_Not_Found() throws SQLException, AccountErrorResponse {
        //given
        Account insert = new Account(2, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 12, 1), "AUD", 1000);
        accountDAO.insert(insert);

        Account toBeUpdate = new Account(100, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2020, 1, 4), "AUD", 0);
        //when
        HttpStatus status = accountClient.updateAccount(toBeUpdate);
        assertThat(status, equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void deleteAccount() throws SQLException, AccountErrorResponse {

        //given
        Account toBeDeleted = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 7, 1), "AUD", 0);
        accountDAO.insert(toBeDeleted);
        //when
        HttpStatus status = accountClient.deleteAccountById(toBeDeleted.getAccountId());

        //then
        assertThat(status, equalTo(HttpStatus.OK));

    }

    @Test
    void deleteAccount_notFound() throws SQLException, AccountErrorResponse {
        //given
        Account toBeDeleted = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 7, 1), "AUD", 0);
        accountDAO.insert(toBeDeleted);

        //when
        HttpStatus status = accountClient.deleteAccountById(100);

        //then
        assertThat(status, equalTo(HttpStatus.NOT_FOUND));
    }
}

