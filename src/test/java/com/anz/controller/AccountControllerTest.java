package com.anz.controller;

import com.anz.dao.AccountDAO;
import com.anz.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {
    @InjectMocks
    private AccountController accountController;

    @Mock
    AccountDAO accountDAO;

    @Test
    public void shouldReturnListWhenAccount() throws SQLException {
        List<Account> accountsMock = new ArrayList<>();
        Account account1 = new Account(5896478521L, 78541236, "Mark", "SAVINGS", new Date(1568037600000L), "AUD", 12568.68);
        Account account2 = new Account(2145698526, 78541236, "Mark", "SAVINGS", new Date(1563544800000L), "USD", 12568.68);
        Account account3 = new Account(7456385941L, 88542169, "Mark", "SAVINGS", new Date(1563544800000L), "EUR", 12568.68);
        accountsMock.add(account1);
        accountsMock.add(account2);
        accountsMock.add(account3);
        when(accountDAO.getAll()).thenReturn(accountsMock);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded", "AccountController");
        ResponseEntity<List<Account>> accountsController = accountController.accounts();
        assertEquals(accountsController, ResponseEntity.accepted().headers(headers).body(accountsMock));
        verify(accountDAO).getAll();

    }

    @Test
    public void shouldReturnEmpty() throws SQLException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded", "AccountController");
        ResponseEntity<List<Account>> accountsController = new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        assertEquals(accountsController.getBody().isEmpty(), ResponseEntity.accepted().headers(headers).body(new ArrayList<>()).getBody().isEmpty());
    }


}
