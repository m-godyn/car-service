package dev.milgodyn.carservice.application;

import dev.milgodyn.carservice.common.LogExecutionTime;
import dev.milgodyn.carservice.dto.CarDto;
import dev.milgodyn.carservice.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping("/car")
    @LogExecutionTime
    public ResponseEntity<CarDto> create(@RequestBody @Valid CarDto dto) {
        log.info("Received request to create car with VIN='{}'", dto.vin());
        var responseBody = carService.create(dto);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{vin}")
                .buildAndExpand(responseBody.vin())
                .toUri();
        log.info("Returning car details for created car with VIN='{}'", responseBody.vin());
        return ResponseEntity
                .created(location)
                .body(responseBody);
    }

    @GetMapping("/car/{vin}")
    @LogExecutionTime
    public ResponseEntity<CarDto> get(@PathVariable String vin) {
        log.info("Received request to get car with VIN='{}'", vin);
        var responseBody = carService.get(vin);
        log.info("Returning car details for VIN='{}'", responseBody.vin());
        return ResponseEntity
                .ok()
                .body(responseBody);
    }

    @GetMapping("/cars")
    @LogExecutionTime
    public ResponseEntity<List<CarDto>> getAll() {
        log.info("Received request to get all cars");
        var responseBody = carService.getAllCars();
        log.info("Returning details for all cars");
        return ResponseEntity
                .ok()
                .body(responseBody);
    }

    @PutMapping("/car/{vin}")
    @LogExecutionTime
    public ResponseEntity<CarDto> update(@PathVariable String vin, @RequestBody CarDto dto) {
        log.info("Received request to update car with VIN='{}'", vin);
        var responseBody = carService.update(vin, dto);
        log.info("Returning car details for updated car with VIN='{}'", responseBody.vin());
        return ResponseEntity
                .ok()
                .body(responseBody);
    }

    @DeleteMapping("/car/{vin}")
    @LogExecutionTime
    public ResponseEntity<Void> delete(@PathVariable String vin) {
        log.info("Received request to delete car with VIN='{}'", vin);
        carService.delete(vin);
        log.info("Returning HTTP 204 response after successful deleted car with VIN='{}'", vin);
        return ResponseEntity
                .noContent()
                .build();
    }
}
