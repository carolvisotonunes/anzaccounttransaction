package com.anz.Controller;

import com.anz.Bean.Transaction;
import com.anz.Helper.ConnectionHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    @RequestMapping(value = "/transaction/{accountNumber}")
    @ResponseBody
    public List<Transaction> transaction(@PathVariable String accountNumber) {
        return ConnectionHelper.getTransactions(accountNumber);
    }
}
