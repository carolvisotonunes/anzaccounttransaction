package com.anz.controller;

import com.anz.DbHelper;
import com.anz.client.TransactionClient;
import com.anz.dao.AccountDAO;
import com.anz.dao.TransactionDAO;
import com.anz.expection.TransactionErrorResponse;
import com.anz.model.Account;
import com.anz.model.Transaction;
import com.anz.responses.TransactionsResponse;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TransactionControllerTest {
    private static final String TRANSACTIONS_URL = "http://localhost:8080/transactions";
    TransactionDAO transactionDAO;
    private RestTemplate restTemplate;
    TransactionClient transactionClient;
    private WebClient webClient;
    private AccountDAO accountDAO;

    @BeforeEach
    public void setUp() throws Exception {

        int port = 8080;
        final String baseUrl = String.format("http://localhost:%s/", port);
        webClient = WebClient.create(baseUrl);
        restTemplate = new RestTemplate();
        transactionDAO = new TransactionDAO();
        accountDAO = new AccountDAO();
        transactionClient = new TransactionClient(webClient);
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


    @Test
    public void retrieveTransactionById() throws SQLException, TransactionErrorResponse {
        // Given
        Account insert = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 12, 1), "AUD", 1000);
        accountDAO.insert(insert);
        Transaction expectedTransaction = new Transaction(1, 1, "Bill", LocalDate.of(2019, 01, 01), "AUD", 1548.24, 0.0, "DEBIT", "desc1");
        transactionDAO.create(expectedTransaction);

        // When
        Transaction account = transactionClient.retrieveTransactionById(1);

        // Then
        Assert.assertThat(account, equalTo(expectedTransaction));
    }

    @Test
    public void retrieveTransactionById_NotFound() throws SQLException, TransactionErrorResponse {
        // Given
        Account insert = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 12, 1), "AUD", 1000);
        accountDAO.insert(insert);

        Transaction expectedTransaction = new Transaction(1, 1, "Bill",
                LocalDate.of(2019, 01, 01), "AUD", 1548.24, 0.0, "DEBIT", "desc1");
        transactionDAO.create(expectedTransaction);

        assertThrows(TransactionErrorResponse.class, () -> transactionClient.retrieveTransactionById(100));
    }


    @Test
    void addNewTransaction() throws SQLException, TransactionErrorResponse {
        // Given
        Account insert = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 12, 1), "AUD", 1000);
        accountDAO.insert(insert);

        Transaction insertTransaction = new Transaction(1, 1, "Bill",
                LocalDate.of(2019, 01, 01), "AUD", 1548.24, 0.0, "DEBIT", "desc1");
        transactionDAO.create(insertTransaction);
        Transaction transaction = new Transaction(1, 1, "Bill",
                LocalDate.of(2019, 01, 01), "AUD", 1548.24, 0.0, "DEBIT", "desc1");
        //then
        Assert.assertThat(transaction, equalTo(insertTransaction));

    }

    @Test
    void addNewTransaction_InvalidInput() {
        //when
        assertThrows(TransactionErrorResponse.class, () -> transactionClient.addNewTransaction(null));
    }

    @Test
    void updateTransaction() throws SQLException, TransactionErrorResponse {
        //given
        Account insert = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 12, 1), "AUD", 1000);
        accountDAO.update(insert);

        Transaction insertTransaction = new Transaction(1, 1, "Bill",
                LocalDate.of(2019, 01, 01), "AUD", 1548.24, 0.0, "DEBIT", "desc1");
        transactionDAO.create(insertTransaction);

        Transaction toBeUpdate = new Transaction(1, 1, "Bill",
                LocalDate.now(), "AUD", 1548.24, 0.0, "DEBIT", "desc1");
        HttpStatus status = transactionClient.updateTransaction(toBeUpdate);
        //then
        Assert.assertThat(status, equalTo(HttpStatus.OK));

    }

    @Test
    void updateTransaction_Not_Found() throws SQLException, TransactionErrorResponse {
        //given
        Account insert = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 12, 1), "AUD", 1000);
        accountDAO.insert(insert);

        Transaction insertTransaction = new Transaction(1, 1, "Bill",
                LocalDate.of(2019, 01, 01), "AUD", 1548.24, 0.0, "DEBIT", "desc1");
        transactionDAO.create(insertTransaction);

        Transaction toBeUpdate = new Transaction(100, 1, "Bill",
                LocalDate.now(), "AUD", 1548.24, 0.0, "DEBIT", "desc1");
        HttpStatus status = transactionClient.updateTransaction(toBeUpdate);
        Assert.assertThat(status, equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void deleteTransaction() throws SQLException, TransactionErrorResponse {
        Account insert = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 12, 1), "AUD", 1000);
        accountDAO.update(insert);

        Transaction toBeDeleted = new Transaction(1, 1, "Bill",
                LocalDate.of(2019, 01, 01), "AUD", 1548.24, 0.0, "DEBIT", "desc1");
        transactionDAO.create(toBeDeleted);

        //when
        HttpStatus status = transactionClient.deleteTransactionById(toBeDeleted.getTransactionId());

        //then
        Assert.assertThat(status, equalTo(HttpStatus.OK));

    }

    @Test
    void deleteTransaction_notFound() throws SQLException, TransactionErrorResponse {
        //given
        Account insert = new Account(1, 78541236, "Mark", "SAVINGS",
                LocalDate.of(2019, 12, 1), "AUD", 1000);
        accountDAO.update(insert);

        Transaction toBeDeleted = new Transaction(1, 1, "Bill",
                LocalDate.of(2019, 01, 01), "AUD", 1548.24, 0.0, "DEBIT", "desc1");
        transactionDAO.create(toBeDeleted);
        //when
        HttpStatus status = transactionClient.deleteTransactionById(100);

        //then
        Assert.assertThat(status, equalTo(HttpStatus.NOT_FOUND));
    }


}
