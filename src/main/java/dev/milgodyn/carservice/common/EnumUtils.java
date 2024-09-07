package dev.milgodyn.carservice.common;

import dev.milgodyn.carservice.exception.InvalidPropertyValueException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.NoSuchElementException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumUtils {

    public static <T extends Enum<T>> T fromValue(String value, Class<T> enumClass, String propertyName) throws NoSuchElementException {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(enumConstant -> enumConstant.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new InvalidPropertyValueException(value, propertyName));
    }
}
