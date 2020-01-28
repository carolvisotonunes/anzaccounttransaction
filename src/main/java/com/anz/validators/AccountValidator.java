package com.anz.validators;

import com.anz.enums.AccountTypeEnum;
import com.anz.enums.CurrencyEnum;
import com.anz.model.Account;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountValidator {
    public List<ValidationError> validate(Account account) {
        List<ValidationError> errors = new ArrayList<>();
        validateAccountId(account.getAccountId(), errors);
        validateCustomerId(account.getCustomerId(), errors);
        validateAccountName(account.getAccountName(), errors);
        validateAccountType(account.getAccountType(), errors);
        validateDate(account.getBalanceDate(), errors);
        validateCurrency(account.getCurrency(), errors);
        validateAvailableBalance(account.getAvailableBalance(), errors);
        return errors;
    }

    private void validateAccountId(long accountId, List<ValidationError> errors) {
        if (accountId < 0) {
            errors.add(new ValidationError("accountId", "Account Id must be positive"));
        }
    }

    private void validateCustomerId(long customerId, List<ValidationError> errors) {
        if (customerId < 0) {
            errors.add(new ValidationError("customerId", "Customer Id must not be a positive number"));
        }
    }

    public void validateAccountName(String accountName, List<ValidationError> errors) {
        if (accountName == null) {
            errors.add(new ValidationError("accountName", "Account Name must not be null"));
        } else if (accountName.trim().isEmpty()) {
            errors.add(new ValidationError("accountName", "Account Name must not be empty"));
        }
    }

    public void validateAccountType(AccountTypeEnum accountType, List<ValidationError> errors) {
        if (accountType.name().trim().isEmpty()){
            errors.add(new ValidationError("accountType", "Account Type values must be Savings/Current"));
        }
    }
    public void validateDate(LocalDate balanceDate, List<ValidationError> errors) {
        if (balanceDate == null) {
            errors.add(new ValidationError("balanceDate", "Balance Date must not be null"));
        }
    }

    public void validateCurrency(CurrencyEnum currency, List<ValidationError> errors) {
        if (currency.name().trim().isEmpty()) {
            errors.add(new ValidationError("accountType", "Currency values must be SGD / AUD"));
        }
    }

    public void validateAvailableBalance(double availableBalance, List<ValidationError> errors) {
        if (availableBalance < 0) {
            errors.add(new ValidationError("availableBalance", "Available Balance must not be a positive value"));
        }
    }
}
