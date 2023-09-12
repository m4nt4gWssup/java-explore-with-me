package ru.practicum.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String mess) {
        super(mess);
    }
}