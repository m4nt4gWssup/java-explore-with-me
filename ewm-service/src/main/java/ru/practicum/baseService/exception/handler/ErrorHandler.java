package ru.practicum.baseService.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.baseService.exception.ConditionsNotMetException;
import ru.practicum.baseService.exception.ConflictException;
import ru.practicum.baseService.exception.NotFoundException;
import ru.practicum.baseService.exception.ValidationException;
import ru.practicum.baseService.exception.error.ErrorResponse;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        return handleException(e, HttpStatus.CONFLICT, "Data integrity violation.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConditionsNotMetException(final ConditionsNotMetException e) {
        return handleException(e, HttpStatus.CONFLICT, "Conditions for the requested operation are not met.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        return handleException(e, HttpStatus.NOT_FOUND, "Required object not found.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBlankException(final MethodArgumentNotValidException e) {
        String field = Objects.requireNonNull(e.getFieldError()).getField();
        return handleException(e, HttpStatus.BAD_REQUEST, String.format("Field: %s. Error: should not be empty. Value: %s", field, e.getFieldValue(field)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        return handleException(e, HttpStatus.BAD_REQUEST, "Malformed request.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        return handleException(e, HttpStatus.BAD_REQUEST, "Malformed request.");
    }

    private ErrorResponse handleException(final Exception e, final HttpStatus status, final String defaultMessage) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                status.toString(),
                defaultMessage,
                e.getMessage());
    }
}