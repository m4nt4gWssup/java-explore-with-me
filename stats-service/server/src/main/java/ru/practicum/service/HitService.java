package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.HitViewDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    HitDto create(HitDto hitDto);

    List<HitViewDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}