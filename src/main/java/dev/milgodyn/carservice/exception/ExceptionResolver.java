package dev.milgodyn.carservice.exception;

import dev.milgodyn.carservice.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(InvalidPropertyValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPropertyValue(HttpServletRequest request, InvalidPropertyValueException e) {
        log.info(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                LocalDateTime.now(),
                                400,
                                e.getMessage(),
                                request.getRequestURI())
                );
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCarNotFound(HttpServletRequest request, CarNotFoundException e) {
        log.info(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorResponse(
                                LocalDateTime.now(),
                                404,
                                e.getMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(CarAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCarAlreadyExistsException(HttpServletRequest request, CarAlreadyExistsException e) {
        log.info(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new ErrorResponse(
                                LocalDateTime.now(),
                                409,
                                e.getMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.info(e.getMessage());

        var invalidFields = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getField)
                .toList();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                LocalDateTime.now(),
                                400,
                                "Invalid parameters given: [%s]".formatted(String.join(", ", invalidFields)),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
        log.error("Unexpected exception! %s".formatted(e.getMessage()));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                LocalDateTime.now(),
                                500,
                                "Internal Server Error. Contact with Administrator and give him request id: '%s'"
                                        .formatted(request.getRequestId()),
                                request.getRequestURI()
                        )
                );
    }
}
