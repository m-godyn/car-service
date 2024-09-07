package dev.milgodyn.carservice.mapper;

import dev.milgodyn.carservice.dto.CarDto;
import dev.milgodyn.carservice.mapper.CarMapper;
import dev.milgodyn.carservice.persistence.entity.CarEntity;
import dev.milgodyn.carservice.persistence.type.FuelType;
import dev.milgodyn.carservice.persistence.type.TransmissionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CarMapperTest {

    private CarMapper carMapper;

    @BeforeEach
    void setUp() {
        carMapper = Mappers.getMapper(CarMapper.class);
    }

    @Test
    void shouldMapDtoToEntityIgnoringCreatedField() {
        // given
        var carDto = new CarDto(
                "1HGCM82633A004352",
                "Toyota",
                "Corolla",
                2020,
                50000,
                FuelType.GASOLINE,
                1.8,
                "Black",
                TransmissionType.MANUAL,
                "John Doe",
                25000.00,
                "XYZ123456",
                LocalDate.of(2020, 5, 15),
                LocalDate.of(2023, 5, 15),
                LocalDate.of(2024, 5, 15),
                LocalDateTime.now() // should be ignored
        );

        // when
        var carEntity = carMapper.toEntity(carDto);

        // then
        assertThat(carEntity.getVin()).isEqualTo("1HGCM82633A004352");
        assertThat(carEntity.getBrand()).isEqualTo("Toyota");
        assertThat(carEntity.getModel()).isEqualTo("Corolla");
        assertThat(carEntity.getProductionYear()).isEqualTo(2020);
        assertThat(carEntity.getMileage()).isEqualTo(50000);
        assertThat(carEntity.getFuelType()).isEqualTo(FuelType.GASOLINE);
        assertThat(carEntity.getEngineCapacity()).isEqualTo(1.8);
        assertThat(carEntity.getColor()).isEqualTo("Black");
        assertThat(carEntity.getTransmission()).isEqualTo(TransmissionType.MANUAL);
        assertThat(carEntity.getOwner()).isEqualTo("John Doe");
        assertThat(carEntity.getPrice()).isEqualTo(25000.00);
        assertThat(carEntity.getRegistrationNumber()).isEqualTo("XYZ123456");
        assertThat(carEntity.getRegistrationDate()).isEqualTo(LocalDate.of(2020, 5, 15));
        assertThat(carEntity.getInsuranceExpirationDate()).isEqualTo(LocalDate.of(2023, 5, 15));
        assertThat(carEntity.getTechnicalInspectionExpirationDate()).isEqualTo(LocalDate.of(2024, 5, 15));
        assertThat(carEntity.getCreated()).isNull();
    }

    @Test
    void shouldMapEntityToDto() {
        // given
        var carEntity = new CarEntity();
        carEntity.setVin("1HGCM82633A004352");
        carEntity.setBrand("Toyota");
        carEntity.setModel("Corolla");
        carEntity.setProductionYear(2020);
        carEntity.setMileage(50000);
        carEntity.setFuelType(FuelType.GASOLINE);
        carEntity.setEngineCapacity(1.8);
        carEntity.setColor("Black");
        carEntity.setTransmission(TransmissionType.MANUAL);
        carEntity.setOwner("John Doe");
        carEntity.setPrice(25000.00);
        carEntity.setRegistrationNumber("XYZ123456");
        carEntity.setRegistrationDate(LocalDate.of(2020, 5, 15));
        carEntity.setInsuranceExpirationDate(LocalDate.of(2023, 5, 15));
        carEntity.setTechnicalInspectionExpirationDate(LocalDate.of(2024, 5, 15));
        carEntity.setCreated(LocalDateTime.now());

        // when
        var carDto = carMapper.toDto(carEntity);

        // then
        assertThat(carDto.vin()).isEqualTo("1HGCM82633A004352");
        assertThat(carDto.brand()).isEqualTo("Toyota");
        assertThat(carDto.model()).isEqualTo("Corolla");
        assertThat(carDto.productionYear()).isEqualTo(2020);
        assertThat(carDto.mileage()).isEqualTo(50000);
        assertThat(carDto.fuelType()).isEqualTo(FuelType.GASOLINE);
        assertThat(carDto.engineCapacity()).isEqualTo(1.8);
        assertThat(carDto.color()).isEqualTo("Black");
        assertThat(carDto.transmission()).isEqualTo(TransmissionType.MANUAL);
        assertThat(carDto.owner()).isEqualTo("John Doe");
        assertThat(carDto.price()).isEqualTo(25000.00);
        assertThat(carDto.registrationNumber()).isEqualTo("XYZ123456");
        assertThat(carDto.registrationDate()).isEqualTo(LocalDate.of(2020, 5, 15));
        assertThat(carDto.insuranceExpirationDate()).isEqualTo(LocalDate.of(2023, 5, 15));
        assertThat(carDto.technicalInspectionExpirationDate()).isEqualTo(LocalDate.of(2024, 5, 15));
        assertThat(carDto.created()).isEqualTo(carEntity.getCreated());
    }

    @Test
    void shouldMapEntityListToDtoList() {
        // given
        var carEntity1 = new CarEntity();
        carEntity1.setVin("1HGCM82633A004352");
        carEntity1.setBrand("Toyota");
        carEntity1.setModel("Corolla");
        carEntity1.setProductionYear(2020);
        carEntity1.setMileage(50000);
        carEntity1.setFuelType(FuelType.GASOLINE);
        carEntity1.setEngineCapacity(1.8);
        carEntity1.setColor("Black");
        carEntity1.setTransmission(TransmissionType.MANUAL);
        carEntity1.setOwner("John Doe");
        carEntity1.setPrice(25000.00);

        var carEntity2 = new CarEntity();
        carEntity2.setVin("2HGCM82633A004353");
        carEntity2.setBrand("Honda");
        carEntity2.setModel("Civic");
        carEntity2.setProductionYear(2021);
        carEntity2.setMileage(30000);
        carEntity2.setFuelType(FuelType.DIESEL);
        carEntity2.setEngineCapacity(2.0);
        carEntity2.setColor("Red");
        carEntity2.setTransmission(TransmissionType.AUTOMATIC);
        carEntity2.setOwner("Jane Doe");
        carEntity2.setPrice(30000.00);

        var carEntities = List.of(carEntity1, carEntity2);

        // when
        var carDtos = carMapper.toDtoList(carEntities);

        // then
        assertThat(carDtos).hasSize(2);

        var carDto1 = carDtos.get(0);
        assertThat(carDto1.vin()).isEqualTo("1HGCM82633A004352");
        assertThat(carDto1.brand()).isEqualTo("Toyota");
        assertThat(carDto1.model()).isEqualTo("Corolla");

        var carDto2 = carDtos.get(1);
        assertThat(carDto2.vin()).isEqualTo("2HGCM82633A004353");
        assertThat(carDto2.brand()).isEqualTo("Honda");
        assertThat(carDto2.model()).isEqualTo("Civic");
    }

    @Test
    void shouldUpdateCarEntityFromDtoIgnoringNullValues() {
        // given
        var existingCarEntity = new CarEntity();
        existingCarEntity.setVin("1HGCM82633A004352");
        existingCarEntity.setBrand("Toyota");
        existingCarEntity.setModel("Corolla");
        existingCarEntity.setProductionYear(2020);
        existingCarEntity.setMileage(50000);
        existingCarEntity.setFuelType(FuelType.GASOLINE);
        existingCarEntity.setEngineCapacity(1.8);
        existingCarEntity.setColor("Black");
        existingCarEntity.setTransmission(TransmissionType.MANUAL);
        existingCarEntity.setOwner("John Doe");
        existingCarEntity.setPrice(25000.00);

        var updateDto = new CarDto(
                "1HGCM82633A004352",
                null,
                null,
                null,
                60000,
                null,
                null,
                null,
                null,
                "Jane Smith",
                27000.00,
                null,
                null,
                null,
                null,
                null
        );

        // when
        carMapper.updateCarFromDto(updateDto, existingCarEntity);

        // then
        assertThat(existingCarEntity.getVin()).isEqualTo("1HGCM82633A004352");
        assertThat(existingCarEntity.getBrand()).isEqualTo("Toyota");
        assertThat(existingCarEntity.getModel()).isEqualTo("Corolla");
        assertThat(existingCarEntity.getProductionYear()).isEqualTo(2020);
        assertThat(existingCarEntity.getMileage()).isEqualTo(60000); // actualized
        assertThat(existingCarEntity.getOwner()).isEqualTo("Jane Smith"); // actualized
        assertThat(existingCarEntity.getPrice()).isEqualTo(27000.00); // actualized
        assertThat(existingCarEntity.getFuelType()).isEqualTo(FuelType.GASOLINE);
        assertThat(existingCarEntity.getEngineCapacity()).isEqualTo(1.8);
    }
}
