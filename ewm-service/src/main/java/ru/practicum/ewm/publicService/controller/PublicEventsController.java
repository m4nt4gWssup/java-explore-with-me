package ru.practicum.ewm.publicService.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.baseService.dto.event.EventFullDto;
import ru.practicum.ewm.baseService.dto.event.EventShortDto;
import ru.practicum.ewm.publicService.dto.RequestParamForEvent;
import ru.practicum.ewm.publicService.service.event.PublicEventsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/events")
@Validated
public class PublicEventsController {

    public final PublicEventsService eventsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<EventShortDto> getAll(@RequestParam(required = false) String text,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) Boolean paid,
                                     @RequestParam(required = false) LocalDateTime rangeStart,
                                     @RequestParam(required = false) LocalDateTime rangeEnd,
                                     @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                     @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(defaultValue = "10") @Positive int size,
                                     HttpServletRequest request) {
        log.info("Получен запрос GET /events c параметрами: text = {}, categories = {}, paid = {}, rangeStart = {}, " +
                        "rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {}", text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        RequestParamForEvent param = RequestParamForEvent.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .request(request)
                .build();
        return eventsService.getAll(param);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto get(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен запрос GET /events/{}", id);
        return eventsService.get(id, request);
    }
}