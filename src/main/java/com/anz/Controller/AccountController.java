package com.anz.Controller;

import com.anz.Bean.Account;
import com.anz.DAO.AccountDAO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@RestController
public class AccountController {


    @RequestMapping("/account")
    public List<Account> account() {
        List<Account> accounts = new ArrayList<Account>();
        AccountDAO accountDAO = new AccountDAO();
        try {
            accounts = accountDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
}
