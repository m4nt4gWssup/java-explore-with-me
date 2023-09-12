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
    public ErrorResponse handleForbiddenException(final ConflictException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.toString(),
                "Целостность нарушена.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleForbiddenException(final ConditionsNotMetException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.toString(),
                "Для запрошенной операции условия не выполняются.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.toString(),
                "Требуемый объект не найден.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBlankException(final MethodArgumentNotValidException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        String field = Objects.requireNonNull(e.getFieldError()).getField();

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "Некорректно составлен запрос.",
                String.format("Поле: %s. Ошибка: не должно быть пустым. Значение: %s", field, e.getFieldValue(field)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "Некорректно составлен запрос.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse expValidation(final ValidationException e) {
        log.error("ERROR 400: {}", e.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "Некорректно составлен запрос.",
                e.getMessage());
    }
}