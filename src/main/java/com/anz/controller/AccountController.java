package com.anz.controller;

import com.anz.dao.AccountDAO;
import com.anz.model.Account;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {
    AccountDAO accountDAO;

    public AccountController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @RequestMapping("/account")
    public ResponseEntity<List<Account>> accounts() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Responded", "AccountController");
            return ResponseEntity.accepted().headers(headers).body(accountDAO.getAll());
        } catch (SQLException e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/account")
    public ResponseEntity<Account> newAccount(@RequestBody Account account) throws SQLException {
        try {
            accountDAO.persist(account);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/account")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) throws SQLException {
        try {
            accountDAO.update(account);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/account/{accountID}")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountID") Long accountId) throws SQLException {
        try {
            accountDAO.delete(accountId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new String(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
