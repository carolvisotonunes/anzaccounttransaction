package com.anz.controller;

import com.anz.dao.TransactionDAO;
import com.anz.model.Transaction;
import com.anz.responses.TransactionsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;

@RestController
public class TransactionController {
    private TransactionDAO transactionDAO;

    public TransactionController(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @GetMapping(value = "/transactions")
    public ResponseEntity<TransactionsResponse> transactions() {
        try {
            return ResponseEntity.ok(new TransactionsResponse(transactionDAO.getAll()));
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/transactions/account/{accountId}")
    public ResponseEntity<TransactionsResponse> transactionsFromAccountId(@PathVariable Long accountId) {
        try {
            return ResponseEntity.ok(new TransactionsResponse(transactionDAO.getAll(accountId)));
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactions/transaction/{transactionId}")
    public ResponseEntity<Transaction> transactionsFromTransactionId(@PathVariable String transactionId) {
        try {
            if (transactionDAO.getTransaction(transactionId)!= null){
                return ResponseEntity.ok(transactionDAO.getTransaction(transactionId));
            } else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> newTransaction(@RequestBody Transaction transaction) {
        try {
            transactionDAO.create(transaction);
            return ResponseEntity.created(URI.create("http://localhost:8080/transactions/transaction/" + transaction.getTransactionId())).body(transaction);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/transactions")
    public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction transaction) {
        try {
            Transaction transactionIdToBeUpdated = transactionDAO.getTransaction(String.valueOf(transaction.getTransactionId()));
            if (transactionIdToBeUpdated != null) {
                transactionDAO.update(transaction);
                return ResponseEntity.ok(transaction);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
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
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
