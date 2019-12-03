package com.anz.controller;

import com.anz.dao.AccountDAO;
import com.anz.model.Account;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {
    AccountDAO accountDAO;
    public AccountController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }
    @RequestMapping("/accounts")
    public ResponseEntity<List<Account>> accounts() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Responded", "AccountController");
            return ResponseEntity.accepted().headers(headers).body(accountDAO.getAll());
        } catch (SQLException e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
