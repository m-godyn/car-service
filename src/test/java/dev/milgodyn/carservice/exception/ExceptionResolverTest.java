package dev.milgodyn.carservice.exception;

import dev.milgodyn.carservice.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

class ExceptionResolverTest {

    private ExceptionResolver exceptionResolver;
    private HttpServletRequest requestMock;

    @BeforeEach
    void setUp() {
        exceptionResolver = new ExceptionResolver();
        requestMock = Mockito.mock(HttpServletRequest.class);
    }

    @Test
    void shouldHandleInvalidPropertyValueException() {
        // given
        var exception = new InvalidPropertyValueException("value", "property");
        when(requestMock.getRequestURI()).thenReturn("/v1/car/invalid");

        // when
        var response = exceptionResolver.handleInvalidPropertyValue(requestMock, exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Invalid value 'value' for property=<property>");
        assertThat(response.getBody().path()).isEqualTo("/v1/car/invalid");
    }

    @Test
    void shouldHandleCarNotFoundException() {
        // given
        var exception = new CarNotFoundException("VIN");
        when(requestMock.getRequestURI()).thenReturn("/v1/car/nonexistent");

        // when
        var response = exceptionResolver.handleCarNotFound(requestMock, exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().error()).isEqualTo("Car with VIN='VIN' could not be found");
        assertThat(response.getBody().path()).isEqualTo("/v1/car/nonexistent");
    }

    @Test
    void shouldHandleCarAlreadyExistsException() {
        // given
        var exception = new CarAlreadyExistsException("VIN");
        when(requestMock.getRequestURI()).thenReturn("/v1/car/exists");

        // when
        var response = exceptionResolver.handleCarAlreadyExistsException(requestMock, exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(CONFLICT);
        assertThat(response.getBody().status()).isEqualTo(409);
        assertThat(response.getBody().error()).isEqualTo("Car with VIN='VIN' already exists");
        assertThat(response.getBody().path()).isEqualTo("/v1/car/exists");
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        // given
        var exception = Mockito.mock(MethodArgumentNotValidException.class);
        var bindingResultMock = Mockito.mock(BindingResult.class);

        when(exception.getBindingResult()).thenReturn(bindingResultMock);
        when(bindingResultMock.getFieldErrors()).thenReturn(List.of(
                new FieldError("carDto", "vin", "must not be blank"),
                new FieldError("carDto", "brand", "must not be blank")
        ));

        when(requestMock.getRequestURI()).thenReturn("/v1/car");

        // when
        var response = exceptionResolver.handleMethodArgumentNotValid(requestMock, exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Invalid parameters given: [vin, brand]");
        assertThat(response.getBody().path()).isEqualTo("/v1/car");
    }

    @Test
    void shouldHandleGenericException() {
        // given
        var exception = new RuntimeException("Unexpected error");
        when(requestMock.getRequestURI()).thenReturn("/v1/car/error");
        when(requestMock.getRequestId()).thenReturn("abc123");

        // when
        var response = exceptionResolver.handleException(requestMock, exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().status()).isEqualTo(500);
        assertThat(response.getBody().error()).isEqualTo("Internal Server Error. Contact with Administrator and give him request id: 'abc123'");
        assertThat(response.getBody().path()).isEqualTo("/v1/car/error");
    }
}
