package com.anz.controller;

import com.anz.dao.AccountDAO;
import com.anz.model.Account;
import com.anz.responses.AccountsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;

@Slf4j
@RestController
public class AccountController {
    private AccountDAO accountDAO;

    public AccountController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @GetMapping("/accounts")
    public ResponseEntity<AccountsResponse> accounts() {
        try {
            return ResponseEntity.ok(new AccountsResponse(accountDAO.getAll()));
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/accounts/{accountID}")
    public ResponseEntity<Account> account(@PathVariable("accountID") Long accountId) {
        try {
            if (accountDAO.getAccountByID(accountId) != null) {
                return ResponseEntity.ok(accountDAO.getAccountByID(accountId));
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/accounts")
    public ResponseEntity<Account> newAccount(@RequestBody Account account) {
        try {
            accountDAO.insert(account);
            return ResponseEntity.created(URI.create("http://localhost:8080/accounts/" + account.getAccountId())).body(account);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/accounts")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        try {
            Account accountIdToBeUpdated = accountDAO.getAccountByID(account.getAccountId());
            if (accountIdToBeUpdated != null) {
                accountDAO.update(account);
                return ResponseEntity.ok(account);
            } else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/accounts/{accountID}")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountID") Long accountId) {
        try {
            Account accountIdToBeDeleted = accountDAO.getAccountByID(accountId);
            if (accountIdToBeDeleted != null) {
                accountDAO.delete(accountId);
                return ResponseEntity.ok().build();
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
