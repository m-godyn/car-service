package dev.milgodyn.carservice.service;

import dev.milgodyn.carservice.dto.CarDto;
import dev.milgodyn.carservice.exception.CarAlreadyExistsException;
import dev.milgodyn.carservice.exception.CarNotFoundException;
import dev.milgodyn.carservice.mapper.CarMapper;
import dev.milgodyn.carservice.persistence.entity.CarEntity;
import dev.milgodyn.carservice.persistence.repository.CarRepository;
import dev.milgodyn.carservice.persistence.type.FuelType;
import dev.milgodyn.carservice.persistence.type.TransmissionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateNewCar_whenGivenValidDto() {
        // given
        var givenLocalDateTime = LocalDateTime.now();
        var givenDto = new CarDto(
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
                null
        );

        var entity = new CarEntity();
        entity.setVin(givenDto.vin());
        entity.setBrand(givenDto.brand());
        entity.setModel(givenDto.model());
        entity.setProductionYear(givenDto.productionYear());
        entity.setMileage(givenDto.mileage());
        entity.setFuelType(givenDto.fuelType());
        entity.setEngineCapacity(givenDto.engineCapacity());
        entity.setColor(givenDto.color());
        entity.setTransmission(givenDto.transmission());
        entity.setOwner(givenDto.owner());
        entity.setPrice(givenDto.price());
        entity.setRegistrationNumber(givenDto.registrationNumber());
        entity.setRegistrationDate(givenDto.registrationDate());
        entity.setInsuranceExpirationDate(givenDto.insuranceExpirationDate());
        entity.setTechnicalInspectionExpirationDate(givenDto.technicalInspectionExpirationDate());
        entity.setCreated(givenLocalDateTime);

        when(carRepository.existsById(givenDto.vin())).thenReturn(false);
        when(carMapper.toEntity(any(CarDto.class))).thenReturn(entity);
        when(carRepository.save(any(CarEntity.class))).thenAnswer(invocation -> {
            CarEntity car = invocation.getArgument(0);
            car.setCreated(givenLocalDateTime);
            return car;
        });
        when(carMapper.toDto(any(CarEntity.class))).thenReturn(givenDto);

        // when
        var actual = underTest.create(givenDto);

        // then
        assertThat(actual).isEqualTo(givenDto);
        verify(carRepository).save(any(CarEntity.class));
        verify(carMapper).toEntity(any(CarDto.class));
        verify(carMapper).toDto(any(CarEntity.class));
    }

    @Test
    void shouldThrowException_whenCarAlreadyExists() {
        // given
        var givenDto = new CarDto(
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
                null
        );

        when(carRepository.existsById(givenDto.vin())).thenReturn(true);

        // when / then
        assertThatThrownBy(() -> underTest.create(givenDto))
                .isInstanceOf(CarAlreadyExistsException.class)
                .hasMessageContaining("Car with VIN='1HGCM82633A004352' already exists");

        verify(carRepository, never()).save(any(CarEntity.class));
        verify(carMapper, never()).toEntity(any(CarDto.class));
    }

    @Test
    void shouldReturnCar_whenGivenExistingVin() {
        // given
        String givenVin = "1HGCM82633A004352";
        var entity = new CarEntity();
        entity.setVin(givenVin);
        entity.setBrand("Toyota");
        entity.setModel("Corolla");
        entity.setProductionYear(2020);
        entity.setMileage(50000);
        entity.setFuelType(FuelType.GASOLINE);
        entity.setEngineCapacity(1.8);
        entity.setColor("Black");
        entity.setTransmission(TransmissionType.MANUAL);
        entity.setOwner("John Doe");
        entity.setPrice(25000.00);
        entity.setRegistrationNumber("XYZ123456");
        entity.setRegistrationDate(LocalDate.of(2020, 5, 15));
        entity.setInsuranceExpirationDate(LocalDate.of(2023, 5, 15));
        entity.setTechnicalInspectionExpirationDate(LocalDate.of(2024, 5, 15));
        entity.setCreated(LocalDateTime.now());

        var expectedDto = new CarDto(
                givenVin,
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
                entity.getCreated()
        );

        when(carRepository.findById(givenVin)).thenReturn(Optional.of(entity));
        when(carMapper.toDto(any(CarEntity.class))).thenReturn(expectedDto);

        // when
        var actual = underTest.get(givenVin);

        // then
        assertThat(actual).isEqualTo(expectedDto);
        verify(carRepository).findById(givenVin);
        verify(carMapper).toDto(any(CarEntity.class));
    }

    @Test
    void shouldThrowException_whenCarNotFound() {
        // given
        String givenVin = "NON_EXISTENT_VIN";
        when(carRepository.findById(givenVin)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> underTest.get(givenVin))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining("Car with VIN='NON_EXISTENT_VIN' could not be found");

        verify(carRepository).findById(givenVin);
        verify(carMapper, never()).toDto(any(CarEntity.class));
    }

    @Test
    void shouldReturnListOfCars() {
        // given
        var car1 = new CarEntity();
        car1.setVin("1HGCM82633A004352");
        car1.setBrand("Toyota");
        car1.setModel("Corolla");
        car1.setProductionYear(2020);
        car1.setMileage(50000);

        var car2 = new CarEntity();
        car2.setVin("2HGCM82633A004353");
        car2.setBrand("Honda");
        car2.setModel("Civic");
        car2.setProductionYear(2021);
        car2.setMileage(30000);

        var carList = List.of(car1, car2);
        var dtoList = List.of(
                new CarDto("1HGCM82633A004352", "Toyota", "Corolla", 2020, 50000, FuelType.GASOLINE, 1.8, "Black", TransmissionType.MANUAL, "John Doe", 25000.00, null, null, null, null, null),
                new CarDto("2HGCM82633A004353", "Honda", "Civic", 2021, 30000, FuelType.DIESEL, 2.0, "Red", TransmissionType.AUTOMATIC, "Jane Doe", 30000.00, null, null, null, null, null)
        );

        when(carRepository.findAll()).thenReturn(carList);
        when(carMapper.toDtoList(carList)).thenReturn(dtoList);

        // when
        var actual = underTest.getAllCars();

        // then
        assertThat(actual).hasSize(2);
        verify(carRepository).findAll();
        verify(carMapper).toDtoList(carList);
    }

    @Test
    void shouldUpdateExistingCar_whenGivenPartialDto() {
        // given
        String givenVin = "1HGCM82633A004352";

        var existingCar = new CarEntity();
        existingCar.setVin(givenVin);
        existingCar.setBrand("Toyota");
        existingCar.setModel("Corolla");
        existingCar.setProductionYear(2020);
        existingCar.setMileage(50000);
        existingCar.setFuelType(FuelType.GASOLINE);
        existingCar.setEngineCapacity(1.8);
        existingCar.setColor("Black");
        existingCar.setTransmission(TransmissionType.MANUAL);
        existingCar.setOwner("John Doe");
        existingCar.setPrice(25000.00);
        existingCar.setRegistrationNumber("XYZ123456");
        existingCar.setRegistrationDate(LocalDate.of(2020, 5, 15));
        existingCar.setInsuranceExpirationDate(LocalDate.of(2023, 5, 15));
        existingCar.setTechnicalInspectionExpirationDate(LocalDate.of(2024, 5, 15));
        existingCar.setCreated(LocalDateTime.now());

        var partialUpdateDto = new CarDto(
                givenVin,
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

        when(carRepository.findById(givenVin)).thenReturn(Optional.of(existingCar));

        doAnswer(invocation -> {
            CarDto dto = invocation.getArgument(0);
            CarEntity entity = invocation.getArgument(1);
            if (dto.mileage() != null) {
                entity.setMileage(dto.mileage());
            }
            if (dto.owner() != null) {
                entity.setOwner(dto.owner());
            }
            if (dto.price() != null) {
                entity.setPrice(dto.price());
            }
            return null;
        }).when(carMapper).updateCarFromDto(any(CarDto.class), any(CarEntity.class));

        when(carRepository.save(any(CarEntity.class))).thenReturn(existingCar);

        when(carMapper.toDto(any(CarEntity.class))).thenAnswer(invocation -> {
            CarEntity car = invocation.getArgument(0);
            return new CarDto(
                    car.getVin(),
                    car.getBrand(),
                    car.getModel(),
                    car.getProductionYear(),
                    car.getMileage(),
                    car.getFuelType(),
                    car.getEngineCapacity(),
                    car.getColor(),
                    car.getTransmission(),
                    car.getOwner(),
                    car.getPrice(),
                    car.getRegistrationNumber(),
                    car.getRegistrationDate(),
                    car.getInsuranceExpirationDate(),
                    car.getTechnicalInspectionExpirationDate(),
                    car.getCreated()
            );
        });

        // when
        var actual = underTest.update(givenVin, partialUpdateDto);

        // then
        assertThat(actual.mileage()).isEqualTo(60000);
        assertThat(actual.owner()).isEqualTo("Jane Smith");
        assertThat(actual.price()).isEqualTo(27000.00);

        assertThat(actual.brand()).isEqualTo("Toyota");
        assertThat(actual.model()).isEqualTo("Corolla");
        assertThat(actual.productionYear()).isEqualTo(2020);
        assertThat(actual.fuelType()).isEqualTo(FuelType.GASOLINE);
        assertThat(actual.engineCapacity()).isEqualTo(1.8);
        assertThat(actual.color()).isEqualTo("Black");
        assertThat(actual.transmission()).isEqualTo(TransmissionType.MANUAL);

        verify(carMapper).updateCarFromDto(partialUpdateDto, existingCar);
        verify(carRepository).save(existingCar);
        verify(carMapper).toDto(existingCar);
    }


    @Test
    void shouldThrowException_whenCarToUpdateNotFound() {
        // given
        String givenVin = "NON_EXISTENT_VIN";
        var updatedDto = new CarDto(
                givenVin,
                "Toyota",
                "Camry",
                2021,
                10000,
                FuelType.HYBRID,
                2.5,
                "Blue",
                TransmissionType.AUTOMATIC,
                "John Smith",
                35000.00,
                "XYZ123456",
                LocalDate.of(2021, 6, 1),
                LocalDate.of(2024, 6, 1),
                LocalDate.of(2025, 6, 1),
                null
        );

        when(carRepository.findById(givenVin)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> underTest.update(givenVin, updatedDto))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining("Car with VIN='NON_EXISTENT_VIN' could not be found");

        verify(carMapper, never()).updateCarFromDto(any(CarDto.class), any(CarEntity.class));
        verify(carRepository, never()).save(any(CarEntity.class));
    }

    @Test
    void shouldDeleteCar_whenGivenExistingVin() {
        // given
        String givenVin = "1HGCM82633A004352";
        var existingCar = new CarEntity();
        existingCar.setVin(givenVin);

        when(carRepository.findById(givenVin)).thenReturn(Optional.of(existingCar));

        // when
        underTest.delete(givenVin);

        // then
        verify(carRepository).delete(existingCar);
    }

    @Test
    void shouldThrowException_whenCarToDeleteNotFound() {
        // given
        String givenVin = "NON_EXISTENT_VIN";
        when(carRepository.findById(givenVin)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> underTest.delete(givenVin))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining("Car with VIN='NON_EXISTENT_VIN' could not be found");
    }
}
