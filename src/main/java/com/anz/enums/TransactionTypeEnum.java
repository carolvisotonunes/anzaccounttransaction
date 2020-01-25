package com.anz.enums;

public enum TransactionTypeEnum {
    CREDIT("Credit"), DEBIT("Debit");

    private String value;

    TransactionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
