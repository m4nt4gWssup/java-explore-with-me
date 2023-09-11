package ru.practicum.ewm.adminService.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminService.dto.RequestParamForEvent;
import ru.practicum.ewm.adminService.service.event.AdminEventsService;
import ru.practicum.ewm.baseService.dto.event.EventFullDto;
import ru.practicum.ewm.baseService.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.baseService.enums.State;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/events")
public class AdminEventsController {

    public final AdminEventsService service;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAll(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<String> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) LocalDateTime rangeStart,
                                     @RequestParam(required = false) LocalDateTime rangeEnd,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получен запрос GET /admin/events");

        List<State> statesEnum = null;
        if (states != null) {
            statesEnum = states.stream().map(State::from).filter(Objects::nonNull).collect(Collectors.toList());
        }

        RequestParamForEvent param = RequestParamForEvent.builder()
                .users(users)
                .states(statesEnum)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();
        return service.getAll(param);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody UpdateEventAdminRequest updateEvent) {
        log.info("Получен запрос PATCH /admin/events/{} на изменение события.", eventId);
        return service.update(eventId, updateEvent);
    }
}