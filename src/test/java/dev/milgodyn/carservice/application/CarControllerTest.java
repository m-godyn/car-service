package dev.milgodyn.carservice.application;

import dev.milgodyn.carservice.dto.CarDto;
import dev.milgodyn.carservice.persistence.type.FuelType;
import dev.milgodyn.carservice.persistence.type.TransmissionType;
import dev.milgodyn.carservice.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @MockBean
    private CarService carService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CarController(carService)).build();
    }

    @Test
    void shouldCreateCar() throws Exception {
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
                LocalDateTime.now()
        );

        when(carService.create(any(CarDto.class))).thenReturn(carDto);

        // when / then
        mockMvc.perform(post("/v1/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "vin": "1HGCM82633A004352",
                          "brand": "Toyota",
                          "model": "Corolla",
                          "production_year": 2020,
                          "mileage": 50000,
                          "fuel_type": "GASOLINE",
                          "engine_capacity": 1.8,
                          "color": "Black",
                          "transmission": "MANUAL",
                          "owner": "John Doe",
                          "price": 25000.00,
                          "registration_number": "XYZ123456",
                          "registration_date": "2020-05-15",
                          "insurance_expiration_date": "2023-05-15",
                          "technical_inspection_expiration_date": "2024-05-15"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.vin").value("1HGCM82633A004352"))
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"))
                .andExpect(jsonPath("$.production_year").value(2020));
    }

    @Test
    void shouldGetCarByVin() throws Exception {
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
                LocalDateTime.now()
        );

        when(carService.get(eq("1HGCM82633A004352"))).thenReturn(carDto);

        // when / then
        mockMvc.perform(get("/v1/car/1HGCM82633A004352")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vin").value("1HGCM82633A004352"))
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"))
                .andExpect(jsonPath("$.production_year").value(2020));
    }

    @Test
    void shouldGetAllCars() throws Exception {
        // given
        var carDto1 = new CarDto(
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
                LocalDateTime.now()
        );

        var carDto2 = new CarDto(
                "2HGCM82633A004353",
                "Honda",
                "Civic",
                2021,
                30000,
                FuelType.DIESEL,
                2.0,
                "Red",
                TransmissionType.AUTOMATIC,
                "Jane Doe",
                30000.00,
                "ABC123456",
                LocalDate.of(2021, 6, 10),
                LocalDate.of(2024, 6, 10),
                LocalDate.of(2025, 6, 10),
                LocalDateTime.now()
        );

        when(carService.getAllCars()).thenReturn(List.of(carDto1, carDto2));

        // when / then
        mockMvc.perform(get("/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vin").value("1HGCM82633A004352"))
                .andExpect(jsonPath("$[0].brand").value("Toyota"))
                .andExpect(jsonPath("$[1].vin").value("2HGCM82633A004353"))
                .andExpect(jsonPath("$[1].brand").value("Honda"));
    }

    @Test
    void shouldUpdateCar() throws Exception {
        // given
        var updatedCarDto = new CarDto(
                "1HGCM82633A004352",
                "Toyota",
                "Corolla",
                2020,
                60000,
                FuelType.GASOLINE,
                1.8,
                "Black",
                TransmissionType.MANUAL,
                "Jane Smith",
                27000.00,
                "XYZ123456",
                LocalDate.of(2020, 5, 15),
                LocalDate.of(2023, 5, 15),
                LocalDate.of(2024, 5, 15),
                LocalDateTime.now()
        );

        when(carService.update(eq("1HGCM82633A004352"), any(CarDto.class))).thenReturn(updatedCarDto);

        // when / then
        mockMvc.perform(put("/v1/car/1HGCM82633A004352")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "owner": "Jane Smith",
                      "price": 27000.00,
                      "mileage": 60000
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vin").value("1HGCM82633A004352"))
                .andExpect(jsonPath("$.owner").value("Jane Smith"))
                .andExpect(jsonPath("$.price").value(27000.00))
                .andExpect(jsonPath("$.mileage").value(60000));
    }


    @Test
    void shouldDeleteCar() throws Exception {
        // given
        // No specific setup needed as we're not returning anything from the service

        // when / then
        mockMvc.perform(delete("/v1/car/1HGCM82633A004352")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
