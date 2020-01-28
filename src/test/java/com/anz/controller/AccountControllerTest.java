package com.anz.controller;

import com.anz.DbHelper;
import com.anz.client.AccountClient;
import com.anz.dao.AccountDAO;
import com.anz.enums.AccountTypeEnum;
import com.anz.enums.CurrencyEnum;
import com.anz.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AccountControllerTest {
    private AccountClient accountClient;
    private AccountDAO accountDAO;
    private WebClient webClient;

    @BeforeEach
    public void setUp() throws Exception {
        int port = 8080;
        final String baseUrl = String.format("http://localhost:%s/", port);
        webClient = WebClient.create(baseUrl);
        accountClient = new AccountClient(webClient);
        accountDAO = new AccountDAO();
        DbHelper dbHelper = new DbHelper();
        dbHelper.truncate();
    }

    // should<DoSomething>When<SomethingHappens>
    // returns<Something>When<SomethingHappens>
    @Test
    public void returnsAccountsWhenRequestSucceeds() throws URISyntaxException, SQLException {
        // Given
        List<Account> accounts = Arrays.asList(
                new Account(1, 78541236, "Mark", AccountTypeEnum.SAVINGS, LocalDate.of(2019, 7, 1), CurrencyEnum.AUD, 0),
                new Account(2, 78541236, "Tim", AccountTypeEnum.SAVINGS, LocalDate.of(2019, 7, 2), CurrencyEnum.AUD, 0),
                new Account(3, 88542169, "Mary", AccountTypeEnum.SAVINGS, LocalDate.of(2019, 7, 3), CurrencyEnum.AUD, 0)
        );
        for (Account account : accounts) {
            accountDAO.insert(account);
        }

        // When
        TestResponse<List<Account>> response = accountClient.retrieveAllAccounts();

        // Then
        assertThat(response.getStatus(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(accounts));
    }

    // shouldRetrieveAccountByIdWhenRequestSucceeds
    @Test
    public void retrieveAccountById() throws SQLException {
        // Given
        Account expectedAccount = new Account(1, 78541236, "Mark", AccountTypeEnum.SAVINGS, LocalDate.of(2019, 7, 1), CurrencyEnum.AUD, 0);
        accountDAO.insert(expectedAccount);

        // When
        TestResponse<Account> response = accountClient.retrieveAccountById(1);

        // Then
        assertThat(response.getBody(), equalTo(expectedAccount));
        assertThat(response.getStatus(), equalTo(HttpStatus.OK));

    }

    // shouldReturnNotFoundWhenAccountIdDoesNotExist
    @Test
    public void retrieveAccountById_NotFound() throws SQLException {
        // Given
        Account expectedAccount = new Account(1, 78541236, "Mark", AccountTypeEnum.SAVINGS, LocalDate.of(2019, 7, 1), CurrencyEnum.AUD, 0);

        // When
        TestResponse<Account> response = accountClient.retrieveAccountById(100);

        // When / Then
        assertThat(response.getStatus(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void addNewAccount() throws SQLException {
        // Given
        Account expectedAccount = new Account(1, 1, "Mark", AccountTypeEnum.SAVINGS,
                LocalDate.now(), CurrencyEnum.AUD, 0);
        // When
        TestResponse<Account> response = accountClient.addNewAccount(expectedAccount);
        // Then
        assertThat(response.getStatus(), equalTo(HttpStatus.CREATED));
        assertThat(response.getBody(), equalTo(expectedAccount));
        assertThat(accountDAO.getAccount(expectedAccount.getAccountId()), equalTo(expectedAccount));
    }

    @Test
    void addNewAccount_InvalidInput() throws SQLException {
        Account account = new Account(-1, -1, null, AccountTypeEnum.SAVINGS,
                null, CurrencyEnum.AUD, -1);
        // When
        TestResponse<Account> response = accountClient.addNewAccount(account);
        // Then
        assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(accountDAO.getAll().size(), equalTo(0));
    }

    @Test
    void updateAccount() throws SQLException {
        // Given
        Account insert = new Account(1, 78541236, "Mark", AccountTypeEnum.SAVINGS, LocalDate.of(2019, 7, 1), CurrencyEnum.AUD, 0);
        accountDAO.insert(insert);

        Account toBeUpdated = new Account(1, 78541236, "Mark", AccountTypeEnum.SAVINGS, LocalDate.now(), CurrencyEnum.AUD, 0);
        // When
        HttpStatus status = accountClient.updateAccount(toBeUpdated);

        // Then
        assertThat(status, equalTo(HttpStatus.OK));
        assertThat(accountDAO.getAccount(toBeUpdated.getAccountId()), equalTo(toBeUpdated));
    }

    @Test
    void updateAccount_Not_Found() throws SQLException {
        //Given
        Account toBeUpdated = new Account(1, 78541236, "Mark", AccountTypeEnum.SAVINGS, LocalDate.of(2019, 7, 1), CurrencyEnum.AUD, 0);
        // When
        HttpStatus status = accountClient.updateAccount(toBeUpdated);

        // Then
        assertThat(status, equalTo(HttpStatus.NOT_FOUND));
        assertThat(accountDAO.getAll().size(), equalTo(0));
    }

    @Test
    void deleteAccount() throws SQLException {
        // Given
        Account toBeDeleted = new Account(1, 78541236, "Mark", AccountTypeEnum.SAVINGS, LocalDate.of(2019, 7, 1), CurrencyEnum.AUD, 0);
        accountDAO.insert(toBeDeleted);

        // When
        HttpStatus status = accountClient.deleteAccountById(toBeDeleted.getAccountId());

        // Then
        assertThat(status, equalTo(HttpStatus.OK));
        assertThat(accountDAO.getAll().size(), equalTo(0));
    }

    @Test
    void deleteAccount_notFound() throws SQLException {
        // Given
        Account account = new Account(1, 78541236, "Mark", AccountTypeEnum.SAVINGS, LocalDate.of(2019, 7, 1), CurrencyEnum.AUD, 0);
        accountDAO.insert(account);

        // When
        HttpStatus status = accountClient.deleteAccountById(100);

        // Then
        assertThat(status, equalTo(HttpStatus.NOT_FOUND));
        assertThat(accountDAO.getAll().size(), equalTo(1));
    }
}

