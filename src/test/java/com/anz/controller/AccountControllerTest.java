package com.anz.controller;

import com.anz.DbHelper;
import com.anz.dao.AccountDAO;
import com.anz.model.Account;
import com.anz.responses.AccountsResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class AccountControllerTest {
    private static final String ACCOUNTS_URL = "http://localhost:8080/accounts";
    private RestTemplate restTemplate;
    private AccountDAO accountDAO;

    @Before
    public void setUp() throws Exception {
        restTemplate = new RestTemplate();
        accountDAO = new AccountDAO();
        DbHelper dbHelper = new DbHelper();
        dbHelper.truncate();
    }

    // TODO: Create tests for all endpoints and test errors!
    @Test
    public void returnsAccountsWhenRequestSucceeds() throws URISyntaxException, SQLException {
        // Given
        List<Account> accounts = Arrays.asList(
                new Account(5896478521L, 78541236, "Mark", "SAVINGS", LocalDate.of(2019, 7, 1), "AUD", 0),
                new Account(2145698526, 78541236, "Mark", "SAVINGS", LocalDate.of(2019, 7, 2), "USD", 0),
                new Account(7456385941L, 88542169, "Mark", "SAVINGS", LocalDate.of(2019, 7, 3), "EUR", 0)
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
}
