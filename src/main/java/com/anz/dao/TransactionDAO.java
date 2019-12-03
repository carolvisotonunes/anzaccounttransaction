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
            setTransactions(resultSet,transactions);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return transactions;
    }

    private void setTransactions(ResultSet resultSet,List<Transaction> transactions) throws SQLException {
        while (resultSet.next()) {
            long transactionId = resultSet.getLong("transactionid");
            long accountNumberB = resultSet.getLong("accountnumber");
            String accountName = resultSet.getString("accountname");
            Timestamp valueDate = resultSet.getTimestamp("valuedate");
            String currency = resultSet.getString("currency");
            double debitAmount = resultSet.getDouble("debitamount");
            double creditAmount = resultSet.getDouble("creditamount");
            String transactionType = resultSet.getString("transactiontype");
            String description = resultSet.getString("description");

            Transaction transaction = new Transaction(transactionId, accountNumberB, accountName, valueDate, currency, debitAmount, creditAmount, transactionType, description);
            transactions.add(transaction);
        }
    }
}
