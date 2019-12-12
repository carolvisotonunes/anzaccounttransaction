package com.anz.dao;

import com.anz.model.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionDAO {
    public List<Transaction> getAll(Long accountId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM transaction WHERE accountnumber = ?")) {
            preparedStatement.setString(1, String.valueOf(accountId));
            ResultSet resultSet = preparedStatement.executeQuery();
            return getTransactionsFrom(resultSet);
        }
    }

    public List<Transaction> getAll() throws SQLException {
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM transaction")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return getTransactionsFrom(resultSet);
        }
    }

    public void create(Transaction transaction) throws SQLException {
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO transaction VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setLong(1, transaction.getTransactionId());
            preparedStatement.setLong(2, transaction.getAccountId());
            preparedStatement.setString(3, transaction.getAccountName());
            preparedStatement.setDate(4, transaction.getValueDate());
            preparedStatement.setString(5, transaction.getCurrency());
            preparedStatement.setDouble(6, transaction.getDebitAmount());
            preparedStatement.setDouble(7, transaction.getCreditAmount());
            preparedStatement.setString(8, transaction.getTransactionType());
            preparedStatement.setString(9, transaction.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    public void update(Transaction transaction) throws SQLException {
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement("UPDATE transaction SET accountnumber=?, accountname=?, valuedate=?, currency=?, debitamount=?, creditamount=?, transactiontype=?, description =? WHERE transactionid=?")) {
            preparedStatement.setLong(1, transaction.getAccountId());
            preparedStatement.setString(2, transaction.getAccountName());
            preparedStatement.setDate(3, transaction.getValueDate());
            preparedStatement.setString(4, transaction.getCurrency());
            preparedStatement.setDouble(5, transaction.getDebitAmount());
            preparedStatement.setDouble(6, transaction.getCreditAmount());
            preparedStatement.setString(7, transaction.getTransactionType());
            preparedStatement.setString(8, transaction.getDescription());
            preparedStatement.setString(9, String.valueOf(transaction.getTransactionId()));
            preparedStatement.executeUpdate();
        }
    }

    public void delete(Long transactionId) throws SQLException {
        final String DELETE_TRANSACTION_SQL = "DELETE FROM transaction WHERE transactionid=?";
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_TRANSACTION_SQL)) {
            preparedStatement.setString(1, String.valueOf(transactionId));
            preparedStatement.executeUpdate();
        }
    }

    public Transaction getTransaction(String transactionId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM transaction WHERE transactionid = ?")) {
            preparedStatement.setString(1, transactionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getTransactionFrom(resultSet);
        }
    }

    private Transaction getTransactionFrom(ResultSet resultSet) throws SQLException {
        Transaction transaction = null;
        while (resultSet.next()) {
            long transactionId = resultSet.getLong("transactionid");
            long accountNumber = resultSet.getLong("accountnumber");
            String accountName = resultSet.getString("accountname");
            Date valueDate = resultSet.getDate("valuedate");
            String currency = resultSet.getString("currency");
            double debitAmount = resultSet.getDouble("debitamount");
            double creditAmount = resultSet.getDouble("creditamount");
            String transactionType = resultSet.getString("transactiontype");
            String description = resultSet.getString("description");
            transaction = new Transaction(transactionId, accountNumber, accountName, valueDate, currency, debitAmount, creditAmount, transactionType, description);
        }
        return transaction;
    }

    private List<Transaction> getTransactionsFrom(ResultSet resultSet) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        while (resultSet.next()) {
            long transactionId = resultSet.getLong("transactionid");
            long accountNumber = resultSet.getLong("accountnumber");
            String accountName = resultSet.getString("accountname");
            Date valueDate = resultSet.getDate("valuedate");
            String currency = resultSet.getString("currency");
            double debitAmount = resultSet.getDouble("debitamount");
            double creditAmount = resultSet.getDouble("creditamount");
            String transactionType = resultSet.getString("transactiontype");
            String description = resultSet.getString("description");

            Transaction transaction = new Transaction(transactionId, accountNumber, accountName, valueDate, currency, debitAmount, creditAmount, transactionType, description);
            transactions.add(transaction);
        }
        return transactions;
    }
}
