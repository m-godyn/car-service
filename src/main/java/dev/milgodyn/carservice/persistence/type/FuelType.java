package dev.milgodyn.carservice.persistence.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.milgodyn.carservice.common.EnumUtils;

public enum FuelType {
    GASOLINE,
    DIESEL,
    ELECTRIC,
    HYBRID;

    @JsonCreator
    public static FuelType fromValue(String value) {
        return EnumUtils.fromValue(value, FuelType.class, "fuel_type");
    }
}
