package com.anz.dao;

import com.anz.model.Account;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountDAO {
    public List<Account> getAll() throws SQLException {
        final String ALL_ACCOUNTS_SQL = "select * from account";
        List<Account> accounts = new ArrayList<Account>();
        // TODO: Investigate how to use a connection pool instead of creating a new connection every time
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(ALL_ACCOUNTS_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            setAccount(accounts, resultSet);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return accounts;
    }

    private void setAccount(List<Account> accounts, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            long accountNumber = resultSet.getLong("accountnumber");
            long customerId = resultSet.getLong("customerid");
            String accountName = resultSet.getString("accountname");
            String accountType = resultSet.getString("accounttype");
            Timestamp balanceDate = resultSet.getTimestamp("balancedate");
            String currency = resultSet.getString("currency");
            double availableBalance = resultSet.getDouble("availablebalance");
            Account account = new Account(accountNumber, customerId, accountName, accountType, balanceDate, currency, availableBalance); // FIXME: Break line
            accounts.add(account);
        }
    }
}