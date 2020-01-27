package com.anz.controller;

import com.anz.dao.AccountDAO;
import com.anz.model.Account;
import com.anz.responses.AccountsResponse;
import com.anz.validators.AccountValidator;
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
        AccountValidator accountValidator = new AccountValidator();
        try {
            List errors = accountValidator.validate(account);
            // FIXME: Put validation in another class and do a unit test
            // Tem que por numa classe
            // AccountValidationErrors errors = accountValidator.validate(account);
            // test happy path -> no errors found (null)
            // accountId -> negative, null, 0
            // customerId -> same as accountId
            // accountName -> null, "", "     "
            // availableBalance -> negative
            // accountType -> null, "", "  ", "invalidAccountType"
            // balanceDate -> null
            // currency -> null, "", "    ", "invalidCurrencyType"

            /**
             * {"errors": [
             *   {"accountId": ["must not be null"]},
             *   {"customerId": ["must not be empty"]},
             *   {"availableBalance": ["must be positive"]},
             *   ...
             * ]}
             */

            if (errors.size() == 0) {
                accountDAO.insert(account);
                return ResponseEntity
                        .created(URI.create("http://localhost:8080/accounts/" + account.getAccountId()))
                        .body(account);
            } else {
                return ResponseEntity.badRequest().body(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/accounts")
    public ResponseEntity<HttpStatus> updateAccount(@RequestBody Account account) {
        AccountValidator accountValidator = new AccountValidator();
        try {
            List errors = accountValidator.validate(account);
            if (errors.size() == 0) {
                Account accountIdToBeUpdated = accountDAO.getAccount(account.getAccountId());
                if (accountIdToBeUpdated != null) {
                    accountDAO.update(account);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return ResponseEntity.badRequest().build();
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
