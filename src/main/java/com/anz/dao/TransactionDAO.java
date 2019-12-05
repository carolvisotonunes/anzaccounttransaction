package com.anz.dao;

import com.anz.model.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionDAO {
    public List<Transaction> getAll(String accountId) throws SQLException {
        final String ALL_TRANSACTIONS_SQL = "SELECT *"
                + "FROM transaction "
                + "WHERE accountnumber = ?";

        List<Transaction> transactions = new ArrayList<Transaction>();
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(ALL_TRANSACTIONS_SQL)) {
            preparedStatement.setString(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            setTransactions(resultSet, transactions);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return transactions;
    }

    private void setTransactions(ResultSet resultSet, List<Transaction> transactions) throws SQLException {
        while (resultSet.next()) {
            long transactionId = resultSet.getLong("transactionid");
            long accountNumberB = resultSet.getLong("accountnumber");
            String accountName = resultSet.getString("accountname");
            Date valueDate = resultSet.getDate("valuedate");
            String currency = resultSet.getString("currency");
            double debitAmount = resultSet.getDouble("debitamount");
            double creditAmount = resultSet.getDouble("creditamount");
            String transactionType = resultSet.getString("transactiontype");
            String description = resultSet.getString("description");

            Transaction transaction = new Transaction(transactionId, accountNumberB, accountName, valueDate, currency, debitAmount, creditAmount, transactionType, description);
            transactions.add(transaction);
        }
    }

    public void persist(Transaction transaction) throws SQLException {
        final String PERSIST_TRANSACTION_SQL = "INSERT INTO transaction VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(PERSIST_TRANSACTION_SQL)) {
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
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

    }

    public void update(Transaction transaction) throws SQLException {
        final String UPDATE_TRANSACTION_SQL = "UPDATE transaction SET accountnumber=?, accountname=?, valuedate=?, currency=?, debitamount=?, creditamount=?, transactiontype=?, description =?" +
                "WHERE transactionid=?";
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_TRANSACTION_SQL)) {
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
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void delete(Long transactionId) throws SQLException {
        final String DELETE_TRANSACTION_SQL = "DELETE FROM transaction WHERE transactionid=?";
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_TRANSACTION_SQL)) {
            preparedStatement.setString(1, String.valueOf(transactionId));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

    }
}
