package dev.milgodyn.carservice.exception;

public class CarAlreadyExistsException extends RuntimeException {
    public CarAlreadyExistsException(String vin) {
        super("Car with VIN='%s' already exists".formatted(vin));
    }
}
