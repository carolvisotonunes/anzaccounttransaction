package com.anz.validators;

import com.anz.enums.CurrencyEnum;
import com.anz.enums.TransactionTypeEnum;
import com.anz.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TransactionValidatorTest {
    TransactionValidator transactionValidator;

    @BeforeEach
    public void setUp(){
        transactionValidator = new TransactionValidator();
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidTransactionIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                1548.24,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidAccountIdIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                1548.24,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidAccountNameIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                1548.24,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidDateIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                1548.24,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidDebitAmountIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                1548.24,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidCreditAmountIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                1548.24,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidDescriptionIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                1548.24,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnValidationErrorsWhenAInValidTransactionIdIsGiven() {
        Transaction transaction = new Transaction(-1,
                1,
                "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                1548.24,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.singletonList(new ValidationError("transactionId", "Transaction Id must be a positive number"))));
    }

    @Test
    public void shouldReturnValidationErrorsWhenAnInValidAccountIdIsGiven() {
        Transaction transaction = new Transaction(1,
                -1,
                "Bill",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                1548.24,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.singletonList(new ValidationError("accountId", "Account Id must be a positive number"))));
    }

    @Test
    public void shouldReturnValidationErrorsWhenAnEmptyAccountNameIdIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                1548.24,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.singletonList(new ValidationError("accountName", "Account Name must not be empty"))));
    }

    @Test
    public void shouldReturnValidationErrorsWhenANullAccountNameIdIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                null,
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                1548.24,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.singletonList(new ValidationError("accountName", "Account Name must not be null"))));
    }

    @Test
    public void shouldReturnValidationErrorsWhenANullDateIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "Tim",
                null,
                CurrencyEnum.AUD.toString(),
                1548.24,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.singletonList(new ValidationError("valueDate", "Value Date must not be null"))));
    }

    @Test
    public void shouldReturnValidationErrorsWhenANegativeNumberToDebitAmountIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "Tim",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                -1,
                0.0,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.singletonList(new ValidationError("debitAmount", "Debit Amount must not be a negative value"))));
    }

    @Test
    public void shouldReturnValidationErrorsWhenANegativeNumberToCreditAmountIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "Tim",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                100,
                -1,
                TransactionTypeEnum.CREDIT.toString(),
                "desc1");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.singletonList(new ValidationError("creditAmount", "Credit Amount must not be a negative value"))));
    }

    @Test
    public void shouldReturnValidationErrorsWhenANullValueToDescriptionIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "Tim",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                100,
                2,
                TransactionTypeEnum.CREDIT.toString(),
                null);
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.singletonList(new ValidationError("description", "Description must not be null"))));
    }

    @Test
    public void shouldReturnValidationErrorsWhenAnEmptyValueToDescriptionIsGiven() {
        Transaction transaction = new Transaction(1,
                1,
                "Tim",
                LocalDate.now(),
                CurrencyEnum.AUD.toString(),
                100,
                2,
                TransactionTypeEnum.CREDIT.toString(),
                "");
        assertThat(transactionValidator.validate(transaction), equalTo(Collections.singletonList(new ValidationError("description", "Description must not be empty"))));
    }

}
