package ru.practicum.baseService.exception;

public class CommentUpdateException extends RuntimeException {
    public CommentUpdateException(String message) {
        super(message);
    }
}
