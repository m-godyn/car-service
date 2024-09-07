package dev.milgodyn.carservice.exception;

public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(String vin) {
        super("Car with VIN='%s' could not be found".formatted(vin));
    }
}
