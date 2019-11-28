package com.anz.Controller;

import com.anz.Bean.Transaction;
import com.anz.DAO.TransactionDAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionController {

    @RequestMapping(value = "/transaction/{accountNumber}")
    @ResponseBody
    public List<Transaction> transaction(@PathVariable String accountNumber) {
        List<Transaction> list = new ArrayList<>();
        TransactionDAO transactionDAO = new TransactionDAO();
        try {
            list = transactionDAO.getAll(accountNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
