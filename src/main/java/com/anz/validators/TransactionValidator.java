package com.anz.validators;

import com.anz.enums.CurrencyEnum;
import com.anz.enums.TransactionTypeEnum;
import com.anz.model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionValidator {
    public List<ValidationError> validate(Transaction transaction) {

        List<ValidationError> errors = new ArrayList<>();
        validateTransactionId(transaction.getTransactionId(), errors);
        validateAccountId(transaction.getAccountId(), errors);
        validateAccountName(transaction.getAccountName(), errors);
        validateDate(transaction.getValueDate(), errors);
        validateCurrency(transaction.getCurrency(), errors);
        validateDebitAmount(transaction.getDebitAmount(), errors);
        validateCreditAmount(transaction.getCreditAmount(), errors);
        validateTransactionType(transaction.getTransactionType(), errors);
        validateDescription(transaction.getDescription(), errors);
        return errors;
    }




    private void validateTransactionId(long transactionId, List<ValidationError> errors) {
        if (transactionId < 0) {
            errors.add(new ValidationError("transactionId", "Transaction Id must be a positive number"));
        }
    }

    private void validateAccountId(long accountId, List<ValidationError> errors) {
        if (accountId < 0){
            errors.add(new ValidationError("accountId", "Account Id must be a positive number"));
        }
    }

    private void validateAccountName(String accountName, List<ValidationError> errors) {
        if (accountName == null){
            errors.add(new ValidationError("accountName", "Account Name must not be null"));
        } else if (accountName.isEmpty()){
            errors.add(new ValidationError("accountName", "Account Name must not be empty"));
        }
    }

    private void validateDate(LocalDate valueDate, List<ValidationError> errors) {
        if (valueDate == null){
            errors.add(new ValidationError("valueDate", "Value Date must not be null"));
        }
    }

    private void validateCurrency(CurrencyEnum currency, List<ValidationError> errors) {
    }

    private void validateDebitAmount(double debitAmount, List<ValidationError> errors) {
        if (debitAmount < 0){
            errors.add(new ValidationError("debitAmount", "Debit Amount must not be a negative value"));
        }
    }

    private void validateCreditAmount(double creditAmount, List<ValidationError> errors) {
        if (creditAmount < 0){
            errors.add(new ValidationError("creditAmount", "Credit Amount must not be a negative value"));
        }
    }

    private void validateTransactionType(TransactionTypeEnum transactionType, List<ValidationError> errors) {
    }

    private void validateDescription(String description, List<ValidationError> errors) {
        if (description == null){
            errors.add(new ValidationError("description", "Description must not be null"));
        } else if (description.trim().isEmpty()){
            errors.add(new ValidationError("description", "Description must not be empty"));
        }
    }


}
