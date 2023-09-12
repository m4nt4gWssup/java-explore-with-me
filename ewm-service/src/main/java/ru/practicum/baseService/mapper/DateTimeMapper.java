package ru.practicum.baseService.mapper;


import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;


@UtilityClass
public class DateTimeMapper {

    public LocalDateTime toLocalDateTime(String stringDate) {
        return LocalDateTime.parse(stringDate);
    }

    public String toStringDate(LocalDateTime date) {
        return date.toString();
    }
}