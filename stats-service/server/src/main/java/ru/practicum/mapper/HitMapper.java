package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.HitDto;
import ru.practicum.model.Hit;

@UtilityClass
public class HitMapper {

    public static Hit toEntity(HitDto dto) {
        return Hit.builder()
                .app(dto.getApp())
                .ip(dto.getIp())
                .uri(dto.getUri())
                .timestamp(dto.getTimestamp())
                .build();

    }

    public static HitDto toDto(Hit entity) {
        return HitDto.builder()
                .id(entity.getId())
                .app(entity.getApp())
                .ip(entity.getIp())
                .uri(entity.getUri())
                .timestamp(entity.getTimestamp())
                .build();
    }
}