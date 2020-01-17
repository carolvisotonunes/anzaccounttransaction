package com.anz.controller;

import com.anz.dao.AccountDAO;
import com.anz.model.Account;
import com.anz.responses.AccountsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;

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
            Account account = accountDAO.getAccount(accountId);
            if (account != null) {
                return ResponseEntity.ok(accountDAO.getAccount(accountId));
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
            if (account.getAccountId() <= 0 || account.getCustomerId() <= 0 || account.getAccountName() == null || account.getAvailableBalance() < 0
                    || account.getAccountType() == null || account.getBalanceDate() == null || account.getCurrency() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                accountDAO.insert(account);
                return ResponseEntity
                        .created(URI.create("http://localhost:8080/accounts/" + account.getAccountId()))
                        .body(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/accounts")
    public ResponseEntity<HttpStatus> updateAccount(@RequestBody Account account) {
        try {
            Account accountIdToBeUpdated = accountDAO.getAccount(account.getAccountId());
            if (accountIdToBeUpdated != null) {
                accountDAO.update(account);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/accounts/{accountID}")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountID") Long accountId) {
        try {
            Account accountIdToBeDeleted = accountDAO.getAccount(accountId);
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
