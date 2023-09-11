package ru.practicum.ewm.publicService.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.baseService.dto.Compilation.CompilationDto;
import ru.practicum.ewm.publicService.service.compilation.PublicCompilationsService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/compilations")
public class PublicCompilationsController {

    public final PublicCompilationsService compilationsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /compilations c параметрами: pinned = {}, from = {}, size = {}", pinned, from, size);
        return compilationsService.getAll(pinned, from, size);
    }

    @GetMapping("/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto get(@PathVariable Long comId) {
        log.info("Получен запрос GET /compilations/{}", comId);
        return compilationsService.get(comId);
    }
}