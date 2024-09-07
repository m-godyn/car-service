package dev.milgodyn.carservice.service;

import dev.milgodyn.carservice.dto.CarDto;
import dev.milgodyn.carservice.exception.CarAlreadyExistsException;
import dev.milgodyn.carservice.exception.CarNotFoundException;
import dev.milgodyn.carservice.mapper.CarMapper;
import dev.milgodyn.carservice.persistence.entity.CarEntity;
import dev.milgodyn.carservice.persistence.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository repository;
    private final CarMapper mapper;

    public CarDto create(CarDto dto) {
        log.info("Attempting to create car with VIN='{}'...", dto.vin());
        if (repository.existsById(dto.vin())) {
            throw new CarAlreadyExistsException(dto.vin());
        }
        var entity = mapper.toEntity(dto);
        entity.setCreated(LocalDateTime.now());
        var savedEntity = repository.save(entity);
        log.info("Successfully created car with VIN='{}'", savedEntity.getVin());
        return mapper.toDto(savedEntity);
    }

    public CarDto get(String vin) {
        log.info("Attemtping to retrieve car with VIN='{}'...", vin);
        var entity = getCarEntity(vin);
        log.info("Successfully retrieved car with VIN='{}'", entity.getVin());
        return mapper.toDto(entity);
    }

    public List<CarDto> getAllCars() {
        log.info("Attempting to retrieve all cars...");
        var entities = repository.findAll();
        log.info("Successfully retrieved all cars");
        return mapper.toDtoList(entities);
    }

    public CarDto update(String vin, CarDto dto) {
        log.info("Attempting to update car with VIN='{}'...", vin);
        var entity = getCarEntity(vin);
        mapper.updateCarFromDto(dto, entity);
        var savedEntity = repository.save(entity);
        log.info("Successfully updated car with VIN='{}'", savedEntity.getVin());
        return mapper.toDto(savedEntity);
    }

    public void delete(String vin) {
        log.info("Attempt to delete car with VIN='{}'...", vin);
        repository.delete(getCarEntity(vin));
        log.info("Successfully deleted car with VIN='{}'", vin);
    }

    private CarEntity getCarEntity(String vin) {
        return repository.findById(vin)
                .orElseThrow(() -> new CarNotFoundException(vin));
    }
}
