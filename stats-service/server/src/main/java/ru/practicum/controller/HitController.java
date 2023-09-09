package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.HitViewDto;
import ru.practicum.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HitController {
    private final HitService hitService;
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<HitViewDto> get(@RequestParam @DateTimeFormat(pattern = FORMAT) LocalDateTime start,
                                @RequestParam @DateTimeFormat(pattern = FORMAT) LocalDateTime end,
                                @RequestParam(required = false) List<String> uris,
                                @RequestParam(defaultValue = "false") Boolean unique) {

        log.info("Получен запрос GET /stats");
        return hitService.get(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto create(@RequestBody HitDto dto) {
        log.info("Получен запрос POST /hit");
        return hitService.create(dto);
    }
}