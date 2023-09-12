package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.HitService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HitController {
    private final HitService hitService;

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> get(@RequestParam(value = "start") String start,
                                  @RequestParam(value = "end") String end,
                                  @RequestParam(value = "uris", defaultValue = "") List<String> uris,
                                  @RequestParam(value = "unique", defaultValue = "false") Boolean unique) {
        log.info("Получен запрос GET /stats");
        return hitService.get(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto create(@RequestBody HitDto statisticDto) {
        log.info("Получен запрос POST /hit");
        return hitService.create(statisticDto);
    }
}