package dev.milgodyn.carservice.exception;

public class InvalidPropertyValueException extends RuntimeException {

    public InvalidPropertyValueException(String value, String property) {
        super("Invalid value '%s' for property=<%s>".formatted(value, property));
    }
}
