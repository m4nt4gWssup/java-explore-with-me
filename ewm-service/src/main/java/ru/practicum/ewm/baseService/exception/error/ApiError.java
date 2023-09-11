package ru.practicum.ewm.baseService.exception.error;

import lombok.Getter;
import ru.practicum.ewm.baseService.mapper.DateTimeMapper;

import java.time.LocalDateTime;

@Getter
public class ApiError {
    private final String status;
    private final String reason;
    private final String message;
    private final String timestamp;

    public ApiError(String status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = DateTimeMapper.toStringDate(LocalDateTime.now());
    }
}