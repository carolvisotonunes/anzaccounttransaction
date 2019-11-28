package com.anz.DAO;

import com.anz.Bean.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

     String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/postgres";
     String username = "carolinenunes";
     String password = "password";
     ResultSet resultSet = null;


    public List<Transaction> getAll(String accountNumber) throws SQLException {
        String sql = "SELECT *"
                + "FROM transaction "
                + "WHERE accountnumber = ?";

        List<Transaction> transactions = new ArrayList<Transaction>();
        try (Connection conn = DriverManager.getConnection(
                jdbcUrl, username, password);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, accountNumber);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long transactionId = resultSet.getLong("transactionid");
                long accountNumberB = resultSet.getLong("accountnumber");
                String accountName = resultSet.getString("accountname");
                String valueDate = resultSet.getString("valuedate");
                String currency = resultSet.getString("currency");
                double debitAmount = resultSet.getDouble("debitamount");
                double creditAmount = resultSet.getDouble("creditamount");
                String transactionType = resultSet.getString("transactiontype");
                String description = resultSet.getString("description");


                Transaction transaction = new Transaction(transactionId, accountNumberB, accountName, valueDate, currency, debitAmount, creditAmount, transactionType, description);
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
