package ru.practicum.baseService.mapper;


import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;


@UtilityClass
public class DateTimeMapper {

    public String toStringDate(LocalDateTime date) {
        return date.toString();
    }
}