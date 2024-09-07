package dev.milgodyn.carservice.persistence.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.milgodyn.carservice.common.EnumUtils;

public enum TransmissionType {
    MANUAL,
    AUTOMATIC;

    @JsonCreator
    public static TransmissionType fromValue(String value) {
        return EnumUtils.fromValue(value, TransmissionType.class, "transmission");
    }
}
