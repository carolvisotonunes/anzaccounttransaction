package com.anz.controller;

import com.anz.DbHelper;
import com.anz.dao.TransactionDAO;
import com.anz.model.Transaction;
import com.anz.responses.TransactionsResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;


public class TransactionControllerTest {
    private static final String TRANSACTIONS_URL = "http://localhost:8080/transactions";
    private RestTemplate restTemplate;
    TransactionDAO transactionDAO;

    @Before
    public void setUp() throws Exception {
        restTemplate = new RestTemplate();
        transactionDAO = new TransactionDAO();
        DbHelper dbHelper = new DbHelper();
        dbHelper.truncate();
    }


    @Test
    public void getTransactionsWhenRequestSucceeds() throws SQLException, URISyntaxException {
        // Given
        List<Transaction> transactions = Arrays.asList(
                new Transaction(5478963254L, 5869712549L, "Bill", LocalDate.of(2019, 01, 01), "AUD", 1548.24, 0.0, "DEBIT", "desc1"),
                new Transaction(254789652L, 5869712549L, "Bill", LocalDate.of(2019, 01, 01), "AUD", 865.47, 0.0, "DEBIT", "desc2")
        );
        for (Transaction transaction : transactions) {
            transactionDAO.create(transaction);
        }
        // When
        ResponseEntity<TransactionsResponse> result = restTemplate.getForEntity(new URI(TRANSACTIONS_URL), TransactionsResponse.class);
        // Then
        assertThat(result.getStatusCodeValue(), equalTo(200));
        assertThat(result.getBody().getTransactions(), containsInAnyOrder(transactions.toArray(new Transaction[0])));

    }
}
