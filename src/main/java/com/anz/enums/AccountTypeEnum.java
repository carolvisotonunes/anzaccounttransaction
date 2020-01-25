package com.anz.enums;

public enum AccountTypeEnum {
    SAVINGS("Savings"), CURRENT("Current");

    private String value;

    AccountTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
