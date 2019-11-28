package com.anz.DAO;

import com.anz.Bean.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

     String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/postgres";
     String username = "carolinenunes";
     String password = "password";
     ResultSet resultSet = null;


    public List<Account> getAll() throws SQLException {
        String sql = "select * from account";
        List<Account> accounts = new ArrayList<Account>();
        try (Connection conn = DriverManager.getConnection(
                jdbcUrl, username, password);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long accountNumber = resultSet.getLong("accountnumber");
                long customerId = resultSet.getLong("customerid");
                String accountName = resultSet.getString("accountname");
                String accountType = resultSet.getString("accounttype");
                String balanceDate = resultSet.getString("balancedate");
                String currency = resultSet.getString("currency");
                double availableBalance = resultSet.getDouble("availablebalance");

                Account account = new Account(accountNumber, customerId, accountName, accountType, balanceDate, currency, availableBalance);
                accounts.add(account);
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }
}