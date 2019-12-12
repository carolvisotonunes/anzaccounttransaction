package com.anz.controller;

import com.anz.dao.AccountDAO;
import com.anz.model.Account;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @InjectMocks
    private AccountController accountController;

    @Mock
    AccountDAO accountDAO;

    @Test
    public void testListAccount() throws SQLException {
        List<Account> accountsMock = new ArrayList<>();
        Account account1 = new Account(5896478521L, 78541236, "Mark", "SAVINGS", new Date(1568037600000L), "AUD", 12568.68);
        Account account2 = new Account(2145698526, 78541236, "Mark", "SAVINGS", new Date(1563544800000L), "USD", 12568.68);
        Account account3 = new Account(7456385941L, 88542169, "Mark", "SAVINGS", new Date(1563544800000L), "EUR", 12568.68);
        accountsMock.add(account1);
        accountsMock.add(account2);
        accountsMock.add(account3);
        when(accountDAO.getAll()).thenReturn(accountsMock);
        ResponseEntity<List<Account>> accountsController = accountController.accounts();
        assertEquals(accountsController, ResponseEntity.ok().body(accountsMock));

    }

    @Test
    public void testAddAccount() throws SQLException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Account account = new Account(5896478500L, 7854123600L, "Someone", "SAVINGS", new Date(1568037600000L), "AUD", 12568.68);
        when(accountDAO.insert(account)).thenReturn(1);
        ResponseEntity<Account> responseEntity = accountController.newAccount(account);
        assertEquals(responseEntity.getStatusCodeValue(), 201);
    }

}
