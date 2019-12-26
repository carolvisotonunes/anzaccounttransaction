package com.anz.controller;

import com.anz.dao.TransactionDAO;
import com.anz.model.Account;
import com.anz.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
public class TransactionController {
    private TransactionDAO transactionDAO;

    public TransactionController(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    // FIXME: Put the printStackTrace in all of the methods
    // FIXME: Return TransactionResponse instead of list of transactions
    // FIXME: Fix types of ResponseEntity to have <>
    @GetMapping(value = "/transactions")
    public ResponseEntity<List<Transaction>> transactions() {
        try {
            return ResponseEntity.ok(transactionDAO.getAll());
        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/transactions/account/{accountId}")
    public ResponseEntity<List<Transaction>> transactionsFromAccountId(@PathVariable Long accountId) {
        try {
            return ResponseEntity.ok(transactionDAO.getAll(accountId));
        } catch (SQLException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactions/transaction/{transactionId}")
    public ResponseEntity<Transaction> transactionsFromTransactionId(@PathVariable String transactionId) {
        try {
            return ResponseEntity.ok(transactionDAO.getTransaction(transactionId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // FIXME: Put inside catch
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> newTransaction(@RequestBody Transaction transaction) {
        try {
            transactionDAO.create(transaction);
            return ResponseEntity.created(URI.create("http://localhost:8080/transactions/transaction/" + transaction.getTransactionId())).body(transaction);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/transactions")
    public ResponseEntity<Account> updateTransaction(@RequestBody Transaction transaction) {
        try {
            transactionDAO.update(transaction);
            return ResponseEntity.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/transactions/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable("transactionId") Long transactionId) {
        try {
            Transaction transactionIdToBeDeleted = transactionDAO.getTransaction(String.valueOf(transactionId));
            if (transactionIdToBeDeleted != null) {
                transactionDAO.delete(transactionId);
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
