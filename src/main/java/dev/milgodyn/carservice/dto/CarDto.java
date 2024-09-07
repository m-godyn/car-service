package dev.milgodyn.carservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.milgodyn.carservice.persistence.type.FuelType;
import dev.milgodyn.carservice.persistence.type.TransmissionType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonPropertyOrder({
        "vin",
        "brand",
        "model",
        "production_year",
        "mileage",
        "fuel_type",
        "engine_capacity",
        "color",
        "transmission",
        "owner",
        "price",
        "registration_number",
        "registration_date",
        "insurance_expiration_date",
        "technical_inspection_expiration_date",
        "created"
})
public record CarDto(@NotBlank String vin,
                     @NotBlank String brand,
                     @NotBlank String model,
                     @Min(1920) @JsonProperty("production_year") Integer productionYear,
                     @NotNull @PositiveOrZero Integer mileage,
                     @NotNull @JsonProperty("fuel_type") FuelType fuelType,
                     @NotNull @Positive @JsonProperty("engine_capacity") Double engineCapacity,
                     @NotBlank String color,
                     @NotNull TransmissionType transmission,
                     @NotBlank String owner,
                     @PositiveOrZero Double price,
                     @JsonProperty("registration_number") String registrationNumber,
                     @JsonProperty("registration_date") LocalDate registrationDate,
                     @JsonProperty("insurance_expiration_date") LocalDate insuranceExpirationDate,
                     @JsonProperty("technical_inspection_expiration_date") LocalDate technicalInspectionExpirationDate,
                     LocalDateTime created) {
}
