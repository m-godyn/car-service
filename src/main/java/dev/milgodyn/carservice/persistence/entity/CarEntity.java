package dev.milgodyn.carservice.persistence.entity;

import dev.milgodyn.carservice.persistence.type.FuelType;
import dev.milgodyn.carservice.persistence.type.TransmissionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "car")
@Getter
@Setter
public class CarEntity {

    @Id
    @Column(nullable = false, unique = true, length = 17)
    String vin;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(nullable = false)
    private int productionYear;

    @Column(nullable = false)
    private int mileage;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Column(nullable = false)
    private double engineCapacity;

    @Column(nullable = false, length = 30)
    private String color;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private TransmissionType transmission;

    @Column(nullable = false, length = 100)
    private String owner;

    @Column(length = 20)
    private String registrationNumber;

    private LocalDate registrationDate;

    private Double price;

    private LocalDate insuranceExpirationDate;

    private LocalDate technicalInspectionExpirationDate;

    @Column(nullable = false)
    private LocalDateTime created;
}
