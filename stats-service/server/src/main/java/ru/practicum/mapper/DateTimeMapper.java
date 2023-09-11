package ru.practicum.mapper;


import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@UtilityClass
public class DateTimeMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String toStringDate(LocalDateTime date) {
        return date.format(formatter);
    }
}