package com.anz.controller;

import com.anz.dao.AccountDAO;
import com.anz.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
public class AccountController {
    private AccountDAO accountDAO;

    public AccountController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> accounts() {
        try {
            return ResponseEntity.ok(accountDAO.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/accounts/{accountID}")
    public ResponseEntity<Account> account(@PathVariable("accountID") Long accountId) {
        try {
            if (accountDAO.getAccount(accountId)!= null)
                return ResponseEntity.ok(accountDAO.getAccount(accountId));
            else
                return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> newAccount(@RequestBody Account account) {
        try {
            if (accountDAO.insert(account) == 1)
                return ResponseEntity.created(URI.create("http://localhost:8080/accounts/" + account.getAccountId())).body(account);
             else
                 return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/accounts")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        try {
            accountDAO.update(account);
            return ResponseEntity.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/accounts/{accountID}")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountID") Long accountId) {
        try {
            Account accountIdToBeDeleted = accountDAO.getAccount(accountId);
            if (accountIdToBeDeleted != null)
                accountDAO.delete(accountId);
            else
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            return ResponseEntity.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
