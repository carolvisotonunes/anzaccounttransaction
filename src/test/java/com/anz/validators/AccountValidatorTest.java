package com.anz.validators;

import com.anz.enums.AccountTypeEnum;
import com.anz.enums.CurrencyEnum;
import com.anz.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class AccountValidatorTest {
    private AccountValidator accountValidator;

    @BeforeEach
    public void setUp() {
        accountValidator = new AccountValidator();
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidAccountIsGiven() {
        Account account = new Account(
                1,
                78541236,
                "Mark",
                AccountTypeEnum.SAVINGS,
                LocalDate.of(2019, 7, 1),
                CurrencyEnum.AUD,
                0
        );
        assertThat(accountValidator.validate(account), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidCustomerIsGiven() {
        Account account = new Account(
                1,
                78541236,
                "Mark",
                AccountTypeEnum.SAVINGS,
                LocalDate.of(2019, 7, 1),
                CurrencyEnum.AUD,
                0
        );
        assertThat(accountValidator.validate(account), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidAccountNameGiven() {
        Account account = new Account(
                1,
                78541236,
                "Mark",
                AccountTypeEnum.SAVINGS,
                LocalDate.of(2019, 7, 1),
                CurrencyEnum.AUD,
                0
        );
        assertThat(accountValidator.validate(account), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidAccountTypeGiven() {
        Account account = new Account(
                1,
                78541236,
                "Mark",
                AccountTypeEnum.SAVINGS,
                LocalDate.of(2019, 7, 1),
                CurrencyEnum.AUD,
                0
        );
        assertThat(accountValidator.validate(account), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidDateGiven() {
        Account account = new Account(
                1,
                78541236,
                "Mark",
                AccountTypeEnum.SAVINGS,
                LocalDate.of(2019, 7, 1),
                CurrencyEnum.AUD,
                0
        );
        assertThat(accountValidator.validate(account), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidCurrencyIsGiven() {
        Account account = new Account(
                1,
                78541236,
                "Mark",
                AccountTypeEnum.SAVINGS,
                LocalDate.of(2019, 7, 1),
                CurrencyEnum.AUD,
                0
        );
        assertThat(accountValidator.validate(account), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldReturnNoValidationErrorsWhenAValidAvailableBalancetIsGiven() {
        Account account = new Account(
                1,
                78541236,
                "Mark",
                AccountTypeEnum.SAVINGS,
                LocalDate.of(2019, 7, 1),
                CurrencyEnum.AUD,
                0
        );
        assertThat(accountValidator.validate(account), equalTo(Collections.emptyList()));
    }

    @Test
    void shouldReturnErrorWhenAccountIdIsNegative() {
        Account account = new Account(
                -1,
                78541236,
                "Mark",
                AccountTypeEnum.SAVINGS,
                LocalDate.of(2019, 7, 1),
                CurrencyEnum.AUD,
                0
        );
        assertThat(accountValidator.validate(account), equalTo(Collections.singletonList(new ValidationError("accountId", "Account Id must be positive"))));
    }

    @Test
    void shouldReturnErrorWhenCostumerIdIsNegative() {
        Account account = new Account(
                1,
                -1,
                "Mark",
                AccountTypeEnum.SAVINGS,
                LocalDate.of(2019, 7, 1),
                CurrencyEnum.AUD,
                0
        );
        assertThat(accountValidator.validate(account), equalTo(Collections.singletonList(new ValidationError("customerId", "Customer Id must not be a positive number"))));
    }

    @Test
    void shouldReturnErrorWhenAccountNameIsEmpty() {
        Account account = new Account(
                1,
                1,
                " ",
                AccountTypeEnum.SAVINGS,
                LocalDate.of(2019, 7, 1),
                CurrencyEnum.AUD,
                0
        );
        assertThat(accountValidator.validate(account), equalTo(Collections.singletonList(new ValidationError("accountName", "Account Name must not be empty"))));
    }


    @Test
    void shouldReturnErrorWhenDateIsNull() {
        Account account = new Account(
                1,
                1,
                "Tim",
                AccountTypeEnum.SAVINGS,
                null,
                CurrencyEnum.AUD,
                0
        );
        assertThat(accountValidator.validate(account), equalTo(Collections.singletonList(new ValidationError("balanceDate", "Balance Date must not be null"))));
    }

    @Test
    void shouldReturnErrorWhenAvailableBalanceIsANegativeNumberNull() {
        Account account = new Account(
                1,
                1,
                "Tim",
                AccountTypeEnum.SAVINGS,
                LocalDate.now(),
                CurrencyEnum.AUD,
                -1
        );
        assertThat(accountValidator.validate(account), equalTo(Collections.singletonList(new ValidationError("availableBalance", "Available Balance must not be a positive value"))));
    }
}
