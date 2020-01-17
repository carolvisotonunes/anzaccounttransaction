package com.anz.controller;

import com.anz.DbHelper;
import com.anz.client.AccountClient;
import com.anz.dao.AccountDAO;
import com.anz.expection.AccountErrorResponse;
import com.anz.model.Account;
import com.anz.responses.AccountsResponse;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountControllerTest {
    private static final String ACCOUNTS_URL = "http://localhost:8080/accounts";
    // FIXME: This should be private
    AccountClient accountClient;
    private RestTemplate restTemplate;
    private AccountDAO accountDAO;
    private WebClient webClient;

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
    public void retrieveAccountById_NotFound() throws SQLException, AccountErrorResponse {
        // Given
        Account expectedAccount = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 7, 1), "AUD", 0);
        accountDAO.insert(expectedAccount);

        // When / Then
        assertThrows(AccountErrorResponse.class, () -> accountClient.retrieveAccountById(100));
    }

    @Test
    void addNewAccount() throws SQLException, AccountErrorResponse {
        // Given
        Account expectedAccount = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 7, 1), "AUD", 0);
        // When
        TestResponse<Account> response = accountClient.addNewAccount(expectedAccount);
        // Then
        assertThat(response.getStatus(), equalTo(HttpStatus.CREATED));
        assertThat(response.getBody(), equalTo(expectedAccount));
        assertThat(accountDAO.getAccount(expectedAccount.getAccountId()), equalTo(expectedAccount));
    }

    @Test
    void addNewAccount_InvalidInput() throws AccountErrorResponse, SQLException {
        Account account = new Account(-1, -1, null, null, null, null, -1);
        // When
        TestResponse<Account> response = accountClient.addNewAccount(account);
        // Then
        assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(accountDAO.getAll().size(), equalTo(0));
    }

    @Test
    void updateAccount() throws SQLException, AccountErrorResponse {
        // Given
        Account insert = new Account(2, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 12, 1), "AUD", 1000);
        accountDAO.insert(insert);

        Account toBeUpdated = new Account(2, 78541236, "Mark", "SAVINGS",
                LocalDate.now(), "AUD", 0);
        // When
        HttpStatus status = accountClient.updateAccount(toBeUpdated);

        // Then
        assertThat(status, equalTo(HttpStatus.OK));
        assertThat(accountDAO.getAccount(toBeUpdated.getAccountId()), equalTo(toBeUpdated));
    }

    @Test
    void updateAccount_Not_Found() throws SQLException, AccountErrorResponse {
        //Given
        Account toBeUpdated = new Account(100, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2020, 1, 4), "AUD", 0);
        // When
        HttpStatus status = accountClient.updateAccount(toBeUpdated);

        // Then
        assertThat(status, equalTo(HttpStatus.NOT_FOUND));
        assertThat(accountDAO.getAll().size(), equalTo(0));
    }

    @Test
    void deleteAccount() throws SQLException, AccountErrorResponse {
        // Given
        Account toBeDeleted = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 7, 1), "AUD", 0);
        accountDAO.insert(toBeDeleted);

        // When
        HttpStatus status = accountClient.deleteAccountById(toBeDeleted.getAccountId());

        // Then
        assertThat(status, equalTo(HttpStatus.OK));
        assertThat(accountDAO.getAll().size(), equalTo(0));
    }

    @Test
    void deleteAccount_notFound() throws SQLException, AccountErrorResponse {
        // Given
        Account account = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 7, 1), "AUD", 0);
        accountDAO.insert(account);

        // When
        HttpStatus status = accountClient.deleteAccountById(100);

        // Then
        assertThat(status, equalTo(HttpStatus.NOT_FOUND));
        assertThat(accountDAO.getAll().size(), equalTo(1));
    }
}

