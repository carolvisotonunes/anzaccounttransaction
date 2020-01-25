package com.anz.enums;

public enum CurrencyEnum {

        SGD("SGD"),
        AUD("AUD");

        private String value;

        CurrencyEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
}
