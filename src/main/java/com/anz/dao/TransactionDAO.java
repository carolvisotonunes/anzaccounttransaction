package com.anz.dao;

import com.anz.enums.CurrencyEnum;
import com.anz.enums.TransactionTypeEnum;
import com.anz.model.Transaction;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionDAO {

    private BasicDataSource ds;

    // FIXME: Credentials should not be in code
    public TransactionDAO() {
        ds = new BasicDataSource();
        ds.setUrl(DatabaseConstants.JDBC_URL);
        ds.setUsername(DatabaseConstants.USERNAME);
        ds.setPassword(DatabaseConstants.PASSWORD);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    public List<Transaction> getAll(Long accountId) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM transaction WHERE accountnumber = ?")) {
            preparedStatement.setString(1, String.valueOf(accountId));
            ResultSet resultSet = preparedStatement.executeQuery();
            return transactionsFrom(resultSet);
        }
    }

    public List<Transaction> getAll() throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM transaction")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return transactionsFrom(resultSet);
        }
    }

    public void create(Transaction transaction) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO transaction VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setLong(1, transaction.getTransactionId());
            preparedStatement.setLong(2, transaction.getAccountId());
            preparedStatement.setString(3, transaction.getAccountName());
            preparedStatement.setObject(4, transaction.getValueDate());
            preparedStatement.setString(5, transaction.getCurrency().name());
            preparedStatement.setDouble(6, transaction.getDebitAmount());
            preparedStatement.setDouble(7, transaction.getCreditAmount());
            preparedStatement.setString(8, transaction.getTransactionType().name());
            preparedStatement.setString(9, transaction.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    public void update(Transaction transaction) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("UPDATE transaction SET accountnumber=?, accountname=?, valuedate=?, currency=?, debitamount=?, creditamount=?, transactiontype=?, description =? WHERE transactionid=?")) {
            preparedStatement.setLong(1, transaction.getAccountId());
            preparedStatement.setString(2, transaction.getAccountName());
            preparedStatement.setObject(3, transaction.getValueDate());
            preparedStatement.setString(4, transaction.getCurrency().name());
            preparedStatement.setDouble(5, transaction.getDebitAmount());
            preparedStatement.setDouble(6, transaction.getCreditAmount());
            preparedStatement.setString(7, transaction.getTransactionType().name());
            preparedStatement.setString(8, transaction.getDescription());
            preparedStatement.setString(9, String.valueOf(transaction.getTransactionId()));
            preparedStatement.executeUpdate();
        }
    }

    public void delete(Long transactionId) throws SQLException {
        final String DELETE_TRANSACTION_SQL = "DELETE FROM transaction WHERE transactionid=?";
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_TRANSACTION_SQL)) {
            preparedStatement.setString(1, String.valueOf(transactionId));
            preparedStatement.executeUpdate();
        }
    }

    public Transaction getTransaction(String transactionId) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM transaction WHERE transactionid = ?")) {
            preparedStatement.setString(1, transactionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return transactionFrom(resultSet);
        }
    }

    private Transaction transactionFrom(ResultSet resultSet) throws SQLException {
        Transaction transaction = null;
        while (resultSet.next()) {
            long transactionId = resultSet.getLong("transactionid");
            long accountNumber = resultSet.getLong("accountnumber");
            String accountName = resultSet.getString("accountname");
            LocalDate valueDate = resultSet.getDate("valuedate").toLocalDate();
            CurrencyEnum currency = CurrencyEnum.valueOf(resultSet.getString("currency"));
            double debitAmount = resultSet.getDouble("debitamount");
            double creditAmount = resultSet.getDouble("creditamount");
            TransactionTypeEnum transactionType = TransactionTypeEnum.valueOf(resultSet.getString("transactiontype"));
            String description = resultSet.getString("description");
            transaction = new Transaction(transactionId, accountNumber, accountName, valueDate, currency, debitAmount, creditAmount, transactionType, description);
        }
        return transaction;
    }

    private List<Transaction> transactionsFrom(ResultSet resultSet) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        while (resultSet.next()) {
            long transactionId = resultSet.getLong("transactionid");
            long accountNumber = resultSet.getLong("accountnumber");
            String accountName = resultSet.getString("accountname");
            LocalDate valueDate = resultSet.getDate("valuedate").toLocalDate();
            CurrencyEnum currency = CurrencyEnum.valueOf(resultSet.getString("currency"));
            double debitAmount = resultSet.getDouble("debitamount");
            double creditAmount = resultSet.getDouble("creditamount");
            TransactionTypeEnum transactionType = TransactionTypeEnum.valueOf(resultSet.getString("transactiontype"));
            String description = resultSet.getString("description");

            Transaction transaction = new Transaction(transactionId, accountNumber, accountName, valueDate, currency, debitAmount, creditAmount, transactionType, description);
            transactions.add(transaction);
        }
        return transactions;
    }
}
