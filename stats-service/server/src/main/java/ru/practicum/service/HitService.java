package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

public interface HitService {

    HitDto create(HitDto hitDto);

    List<ViewStatsDto> get(String startFormat, String endFormat, List<String> uris, boolean unique);
}