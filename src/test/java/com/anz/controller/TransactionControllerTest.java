package com.anz.controller;

import com.anz.DbHelper;
import com.anz.client.TransactionClient;
import com.anz.dao.AccountDAO;
import com.anz.dao.TransactionDAO;
import com.anz.enums.AccountTypeEnum;
import com.anz.enums.CurrencyEnum;
import com.anz.enums.TransactionTypeEnum;
import com.anz.model.Account;
import com.anz.model.Transaction;
import com.anz.responses.TransactionsResponse;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class TransactionControllerTest {
    private TransactionDAO transactionDAO;
    private TransactionClient transactionClient;
    private WebClient webClient;
    private AccountDAO accountDAO;

    @BeforeEach
    public void setUp() throws Exception {
        int port = 8080;
        final String baseUrl = String.format("http://localhost:%s/", port);
        webClient = WebClient.create(baseUrl);
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
                new Transaction(1l, 1l, "Bill", LocalDate.now(),
                        CurrencyEnum.AUD.toString(), 1548.24, 0.0, TransactionTypeEnum.CREDIT.toString(), "desc1"),
                new Transaction(2l, 1l, "Bill", LocalDate.now(),
                        CurrencyEnum.AUD.toString(), 865.47, 0.0, TransactionTypeEnum.CREDIT.toString(), "desc2")
        );
        for (Transaction transaction : transactions) {
            transactionDAO.create(transaction);
        }

        // When
        TestResponse<List<Transaction>> result = transactionClient.retrieveAllTransaction();

        // Then
        assertThat(result.getStatus(), equalTo(HttpStatus.OK));
        assertThat(result.getBody(), equalTo(transactions));
    }

    @Test
    public void retrieveTransactionById() throws SQLException {
        // Given
        Account insert = new Account(1, 78541236, "Bill", AccountTypeEnum.SAVINGS.toString(),
                LocalDate.of(2019, 12, 1), CurrencyEnum.AUD.toString(), 1000);
        accountDAO.insert(insert);

        Transaction expectedTransaction = new Transaction(1, 1, "Bill",
                LocalDate.of(2019, 01, 01),
                CurrencyEnum.AUD.toString(), 1548.24, 0.0, TransactionTypeEnum.CREDIT.toString(), "desc1");
        transactionDAO.create(expectedTransaction);

        // When
        TestResponse<Transaction> response = transactionClient.retrieveTransactionById(expectedTransaction.getTransactionId());

        // Then
        Assert.assertThat(response.getBody(), equalTo(expectedTransaction));
        Assert.assertThat(response.getStatus(), equalTo(HttpStatus.OK));
    }

    @Test
    public void retrieveTransactionById_NotFound() throws SQLException {
        // Given
        Account insert = new Account(1, 78541236, "Bill", AccountTypeEnum.SAVINGS.toString(),
                LocalDate.of(2019, 12, 1), CurrencyEnum.AUD.toString(), 1000);
        accountDAO.insert(insert);

        Transaction expectedTransaction = new Transaction(1, 1, "Bill",
                LocalDate.of(2019, 01, 01),
                CurrencyEnum.AUD.toString(), 1548.24, 0.0, TransactionTypeEnum.CREDIT.toString(), "desc1");
        transactionDAO.create(expectedTransaction);

        // When
        TestResponse<Transaction> response = transactionClient.retrieveTransactionById(100l);

        // Then
        Assert.assertThat(response.getStatus(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void addNewTransaction() throws SQLException {
        // Given
        Account insert = new Account(1, 78541236, "Bill", AccountTypeEnum.SAVINGS.toString(),
                LocalDate.of(2019, 12, 1), CurrencyEnum.AUD.toString(), 1000);
        accountDAO.insert(insert);
        Transaction expectedTransaction = new Transaction(1, 1, "Bill",
                LocalDate.of(2019, 01, 01),
                CurrencyEnum.AUD.toString(), 1548.24, 0.0, TransactionTypeEnum.CREDIT.toString(), "desc1");
        transactionDAO.create(expectedTransaction);


        TestResponse<Transaction> response = transactionClient.addNewTransaction(expectedTransaction);

        // Then
        assertThat(response.getStatus(), equalTo(HttpStatus.CREATED));
        assertThat(response.getBody(), equalTo(expectedTransaction));
        assertThat(transactionDAO.getTransaction(String.valueOf(expectedTransaction.getTransactionId())), equalTo(expectedTransaction));

    }

    @Test
    void addNewTransaction_InvalidInput() throws SQLException {
        //Given
        Transaction expectedTransaction = new Transaction(-11, -1, "Bill",
                null, CurrencyEnum.AUD.toString(), 1548.24, -10, TransactionTypeEnum.CREDIT.toString(), null);
        // When
        TestResponse<Transaction> response = transactionClient.addNewTransaction(expectedTransaction);

        // Then
        assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(transactionDAO.getAll().size(), equalTo(0));
    }

    @Test
    void updateTransaction() throws SQLException {
        //Given
        Account insert = new Account(1, 78541236, "Bill", AccountTypeEnum.SAVINGS.toString(),
                LocalDate.of(2019, 12, 1), CurrencyEnum.AUD.toString(), 1000);
        accountDAO.insert(insert);

        Transaction expectedTransaction = new Transaction(1, 1, "Bill",
                LocalDate.of(2019, 01, 01),
                CurrencyEnum.AUD.toString(), 1548.24, 0.0, TransactionTypeEnum.CREDIT.toString(), "desc1");
        transactionDAO.create(expectedTransaction);

        Transaction toBeUpdated = new Transaction(1, 1, "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(), 1548.24, 0.0, TransactionTypeEnum.CREDIT.toString(), "desc1");
        HttpStatus status = transactionClient.updateTransaction(toBeUpdated);
        //then
        assertThat(status, equalTo(HttpStatus.OK));
        assertThat(transactionDAO.getTransaction(String.valueOf(toBeUpdated.getTransactionId())), equalTo(toBeUpdated));

    }

    @Test
    void updateTransaction_Not_Found() throws SQLException {
        //Given
        Transaction toBeUpdated = new Transaction(1, 1, "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(), 1548.24, 0.0, TransactionTypeEnum.CREDIT.toString(), "desc1");

        //When
        HttpStatus status = transactionClient.updateTransaction(toBeUpdated);

        assertThat(status, equalTo(HttpStatus.NOT_FOUND));
        assertThat(transactionDAO.getAll().size(), equalTo(0));
    }

    @Test
    void deleteTransaction() throws SQLException {
        Account insert = new Account(1, 78541236, "Bill", AccountTypeEnum.SAVINGS.toString(),
                LocalDate.of(2019, 12, 1), CurrencyEnum.AUD.toString(), 1000);
        accountDAO.insert(insert);

        Transaction toBeDeleted = new Transaction(1, 1, "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(), 1548.24, 0.0, TransactionTypeEnum.CREDIT.toString(), "desc1");
        transactionDAO.create(toBeDeleted);

        //when
        HttpStatus status = transactionClient.deleteTransactionById(toBeDeleted.getTransactionId());

        //then
        assertThat(status, equalTo(HttpStatus.OK));
        assertThat(transactionDAO.getAll().size(), equalTo(0));

    }

    @Test
    void deleteTransaction_notFound() throws SQLException {
        //given
        Account insert = new Account(1, 78541236, "Bill", AccountTypeEnum.SAVINGS.toString(),
                LocalDate.of(2019, 12, 1), CurrencyEnum.AUD.toString(), 1000);
        accountDAO.update(insert);

        Transaction toBeDeleted = new Transaction(1, 1, "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(), 1548.24, 0.0, TransactionTypeEnum.CREDIT.toString(), "desc1");
        transactionDAO.create(toBeDeleted);
        //when
        HttpStatus status = transactionClient.deleteTransactionById(100);

        //then
        assertThat(status, equalTo(HttpStatus.NOT_FOUND));
        assertThat(transactionDAO.getAll().size(), equalTo(1));
    }
}
