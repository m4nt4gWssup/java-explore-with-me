package ru.practicum.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ResourceNotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.exception.error.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse expNotFound(final ResourceNotFoundException exception) {
        log.error("ERROR 404: {}", exception.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), "The required object was not found.",
                exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler({ConstraintViolationException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse expValidation(final RuntimeException exception) {
        log.error("ERROR 400: {}", exception.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Incorrectly made request.",
                exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class,
            HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse expConflict(final RuntimeException exception) {
        log.error("ERROR 409: {}", exception.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT.getReasonPhrase(),
                "For the requested operation the conditions are not met.",
                exception.getMessage(), LocalDateTime.now());
    }
}