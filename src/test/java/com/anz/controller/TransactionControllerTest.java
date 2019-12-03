package com.anz.controller;

import com.anz.dao.TransactionDAO;
import com.anz.model.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    TransactionDAO transactionDAO;

    @Test
    public void shouldReturnListWhenTransactionWithAccountAtURL() throws SQLException {
        List<Transaction> transactionMock = new ArrayList<>();
        Transaction transaction1 = new Transaction(5478963254L, 5869712549L, "Bill", Timestamp.valueOf("2019-01-01 12:30:40"), "AUD", 1548.24, 0.0, "DEBIT", "desc1");
        Transaction transaction2 = new Transaction(254789652L, 5869712549L, "Bill", Timestamp.valueOf("2019-01-01 11:20:40"), "AUD", 865.47, 0.0, "DEBIT", "desc2");
        transactionMock.add(transaction1);
        transactionMock.add(transaction2);
        when(transactionDAO.getAll("5869712549")).thenReturn(transactionMock);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded", "TransactionController");
        ResponseEntity<List<Transaction>> transactionsController = transactionController.transactions("5869712549");
        assertEquals(transactionsController, ResponseEntity.accepted().headers(headers).body(transactionMock));
        verify(transactionDAO).getAll("5869712549");
    }

    @Test
    public void shouldReturnEmptyListWhenTheAccountIDIsEmpty() throws SQLException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded", "Transaction");
        ResponseEntity<List<Transaction>> transactionsController = new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        assertEquals(transactionsController.getBody().isEmpty(), ResponseEntity.accepted().headers(headers).body(new ArrayList<>()).getBody().isEmpty());
    }

}
