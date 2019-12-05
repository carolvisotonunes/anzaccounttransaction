package com.anz.controller;

import com.anz.dao.TransactionDAO;
import com.anz.model.Account;
import com.anz.model.Transaction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionController {
    TransactionDAO transactionDAO;

    public TransactionController(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @RequestMapping(value = "/transaction/{accountId}")
    public ResponseEntity<List<Transaction>> transactions(@PathVariable String accountId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Responded", "TransactionController");
            return ResponseEntity.accepted().headers(headers).body(transactionDAO.getAll(accountId));
        } catch (SQLException e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transaction")
    public ResponseEntity<Transaction> newTransaction (@RequestBody Transaction transaction) throws SQLException {
        try {
            transactionDAO.persist(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/transaction")
    public ResponseEntity<Account> updateTransaction(@RequestBody Transaction transaction) throws SQLException {
        try {
            transactionDAO.update(transaction);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/transactions/{transactionID}")
    public ResponseEntity<String> deleteTransaction (@PathVariable("transactionId") Long transactionId) throws SQLException {
        try {
            transactionDAO.delete(transactionId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new String(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
