package com.anz.DAO;

import com.anz.Bean.Account;
import com.anz.Helper.ConnectionHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public List<Account> getAll() throws SQLException {
        List<Account> accounts = new ArrayList<Account>();

        return ConnectionHelper.getAccounts();
    }
}