package com.learning.springboot.checklistapi.exception;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class CustomExceptionalHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(Exception ex, WebRequest request)
        throws Exception {
        log.error("Um erro foi encontrado na chamada da API: {}", ex);
        return new ResponseEntity<>(new ExceptionalResponse(
                LocalDateTime.now(), ex.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<ExceptionalResponse> handleValidationException(ValidationException validationException) {
        log.error("Um erro de validação foi encontrado na chamada da API: {}", validationException);
        return new ResponseEntity<>(new ExceptionalResponse(
                LocalDateTime.now(), validationException.getMessage(),
                HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }
}
