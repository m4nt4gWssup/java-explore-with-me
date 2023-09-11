package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    HitDto create(HitDto hitDto);

    List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}