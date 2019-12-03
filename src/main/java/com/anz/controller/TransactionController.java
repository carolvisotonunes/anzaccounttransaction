package com.anz.controller;

import com.anz.dao.TransactionDAO;
import com.anz.model.Transaction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionController {
    TransactionDAO transactionDAO;

    public TransactionController(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @RequestMapping(value = "/transactions/{accountId}")
    public ResponseEntity<List<Transaction>> transactions(@PathVariable String accountId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Responded", "TransactionController");
            return ResponseEntity.accepted().headers(headers).body(transactionDAO.getAll(accountId));
        } catch (SQLException e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
