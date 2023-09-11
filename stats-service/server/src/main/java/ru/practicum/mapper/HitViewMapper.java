package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.HitViewDto;
import ru.practicum.model.ViewStats;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class HitViewMapper {

    public static ViewStats toEntity(HitViewDto dto) {
        return ViewStats.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .hits(dto.getHits())
                .build();
    }

    public static HitViewDto toDto(ViewStats entity) {
        return HitViewDto.builder()
                .app(entity.getApp())
                .uri(entity.getUri())
                .hits(entity.getHits())
                .build();
    }

    public static List<HitViewDto> toDtoList(List<ViewStats> viewStats) {
        return viewStats.stream().map(HitViewMapper::toDto).collect(Collectors.toList());
    }
}