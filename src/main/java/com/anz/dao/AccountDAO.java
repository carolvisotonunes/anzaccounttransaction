package com.anz.dao;

import com.anz.model.Account;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountDAO {

    private BasicDataSource ds;

    public AccountDAO() {
        ds = new BasicDataSource();
        ds.setUrl(ConnectionDAO.JDBC_URL);
        ds.setUsername(ConnectionDAO.USERNAME);
        ds.setPassword(ConnectionDAO.PASSWORD);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    public List<Account> getAll() throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM account")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return getAccounts(resultSet);
        }
    }

    public int insert(Account account) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO account VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setLong(1, account.getAccountId());
            preparedStatement.setLong(2, account.getCustomerId());
            preparedStatement.setString(3, account.getAccountName());
            preparedStatement.setString(4, account.getAccountType());
            preparedStatement.setDate(5, account.getBalanceDate());
            preparedStatement.setString(6, account.getCurrency());
            preparedStatement.setDouble(7, account.getAvailableBalance());
            return preparedStatement.executeUpdate();
        }
    }

    public void update(Account account) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("UPDATE account SET customerid=?, accountname=?, accounttype=?, balancedate=?, currency=?, availablebalance=? " +
                     "WHERE accountnumber=?")) {
            preparedStatement.setLong(1, account.getCustomerId());
            preparedStatement.setString(2, account.getAccountName());
            preparedStatement.setString(3, account.getAccountType());
            preparedStatement.setDate(4, account.getBalanceDate());
            preparedStatement.setString(5, account.getCurrency());
            preparedStatement.setDouble(6, account.getAvailableBalance());
            preparedStatement.setString(7, String.valueOf(account.getAccountId()));
            preparedStatement.executeUpdate();
        }
    }

    public void delete(Long accountId) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM account WHERE accountnumber=?")) {
            preparedStatement.setString(1, String.valueOf(accountId));
            preparedStatement.executeUpdate();
        }
    }

    public Account getAccount(Long accountId) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM account WHERE accountnumber = ?")) {
            preparedStatement.setString(1, String.valueOf(accountId));
            ResultSet resultSet = preparedStatement.executeQuery();
            return getAccount(resultSet);
        }

    }

    private List<Account> getAccounts(ResultSet resultSet) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        while (resultSet.next()) {
            long accountNumber = resultSet.getLong("accountnumber");
            long customerId = resultSet.getLong("customerid");
            String accountName = resultSet.getString("accountname");
            String accountType = resultSet.getString("accounttype");
            Date balanceDate = resultSet.getDate("balancedate");
            String currency = resultSet.getString("currency");
            double availableBalance = resultSet.getDouble("availablebalance");
            Account account = new Account(accountNumber, customerId, accountName, accountType,
                    balanceDate, currency, availableBalance);
            accounts.add(account);
        }
        return accounts;
    }

    private Account getAccount(ResultSet resultSet) throws SQLException {
        Account account = null;
        while (resultSet.next()) {
            long accountNumber = resultSet.getLong("accountnumber");
            long customerId = resultSet.getLong("customerid");
            String accountName = resultSet.getString("accountname");
            String accountType = resultSet.getString("accounttype");
            Date balanceDate = resultSet.getDate("balancedate");
            String currency = resultSet.getString("currency");
            double availableBalance = resultSet.getDouble("availablebalance");
            account = new Account(accountNumber, customerId, accountName, accountType,
                    balanceDate, currency, availableBalance);
        }
        return account;
    }
}