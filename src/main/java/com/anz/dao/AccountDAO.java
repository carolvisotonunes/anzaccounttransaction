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
            Date balanceDate = resultSet.getDate("balancedate");
            String currency = resultSet.getString("currency");
            double availableBalance = resultSet.getDouble("availablebalance");
            Account account = new Account(accountNumber, customerId, accountName, accountType,
                                         balanceDate, currency, availableBalance);
            accounts.add(account);
        }
    }

    public void persist(Account account) throws SQLException {
        final String PERSIST_ACCOUNT_SQL = "INSERT INTO account VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(PERSIST_ACCOUNT_SQL)) {
            preparedStatement.setLong(1, account.getAccountId());
            preparedStatement.setLong(2, account.getCustomerId());
            preparedStatement.setString(3, account.getAccountName());
            preparedStatement.setString(4, account.getAccountType());
            preparedStatement.setDate(5, account.getBalanceDate());
            preparedStatement.setString(6, account.getCurrency());
            preparedStatement.setDouble(7, account.getAvailableBalance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

    }

    public void update(Account account) throws SQLException {
        final String UPDATE_ACCOUNT_SQL = "UPDATE account SET customerid=?, accountname=?, accounttype=?, balancedate=?, currency=?, availablebalance=? " +
                "WHERE accountnumber=?";
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_ACCOUNT_SQL)) {
            preparedStatement.setLong(1, account.getCustomerId());
            preparedStatement.setString(2, account.getAccountName());
            preparedStatement.setString(3, account.getAccountType());
            preparedStatement.setDate(4, account.getBalanceDate());
            preparedStatement.setString(5, account.getCurrency());
            preparedStatement.setDouble(6, account.getAvailableBalance());
            preparedStatement.setString(7, String.valueOf(account.getAccountId()));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void delete(Long accountId) throws SQLException {
        final String DELETE_ACCOUNT_SQL = "DELETE FROM account WHERE accountnumber=?";
        try (Connection conn = DriverManager.getConnection(ConnectionDAO.JDBCUrl, ConnectionDAO.USERNAME, ConnectionDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_ACCOUNT_SQL)) {
            preparedStatement.setString(1, String.valueOf(accountId));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

    }
}