package com.anz.validators;

import java.util.Objects;

public class ValidationError {

    private String field;
    private String message;

    public ValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "field='" + field + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationError that = (ValidationError) o;
        return field.equals(that.field) &&
                message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, message);
    }
}
