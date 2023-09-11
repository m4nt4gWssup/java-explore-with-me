package ru.practicum.ewm.baseService.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.baseService.exception.ConditionsNotMetException;
import ru.practicum.ewm.baseService.exception.ConflictException;
import ru.practicum.ewm.baseService.exception.NotFoundException;
import ru.practicum.ewm.baseService.exception.ValidationException;
import ru.practicum.ewm.baseService.exception.error.ApiError;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleForbiddenException(final ConflictException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT.toString(),
                "Целостность нарушена.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleForbiddenException(final ConditionsNotMetException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT.toString(),
                "Для запрошенной операции условия не выполняются.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        return new ApiError(
                HttpStatus.NOT_FOUND.toString(),
                "Требуемый объект не найден.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBlankException(final MethodArgumentNotValidException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        String field = Objects.requireNonNull(e.getFieldError()).getField();

        return new ApiError(
                HttpStatus.BAD_REQUEST.toString(),
                "Некорректно составлен запрос.",
                String.format("Поле: %s. Ошибка: не должно быть пустым. Значение: %s", field, e.getFieldValue(field)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());

        return new ApiError(
                HttpStatus.BAD_REQUEST.toString(),
                "Некорректно составлен запрос.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError expValidation(final ValidationException e) {
        log.error("ERROR 400: {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST.toString(),
                "Некорректно составлен запрос.",
                e.getMessage());
    }
}