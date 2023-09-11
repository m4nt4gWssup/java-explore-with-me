package ru.practicum.ewm.privateService.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.baseService.dto.event.*;
import ru.practicum.ewm.baseService.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.baseService.enums.Status;
import ru.practicum.ewm.baseService.exception.ConflictException;
import ru.practicum.ewm.privateService.service.event.PrivateEventsService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/events")
public class PrivateEventsController {

    public final PrivateEventsService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<EventShortDto> getAll(@PathVariable Long userId,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /users{}/events c параметрами: from = {}, size = {}", userId, from, size);
        return service.getAll(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto get(@PathVariable Long userId,
                            @PathVariable Long eventId) {
        log.info("Получен запрос GET /users{}/events/{}", userId, eventId);
        return service.get(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}/requests", userId, eventId);
        return service.getRequests(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId,
                               @RequestBody @Valid NewEventDto eventDto) {
        log.info("Получен запрос POST /users/{}/events c новым событием: {}", userId, eventDto);
        return service.create(userId, eventDto);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long userId, @PathVariable Long eventId,
                               @RequestBody @Valid UpdateEventUserRequest eventDto) {
        log.info("Получен запрос PATCH /users/{}/events/{eventId}" +
                " c обновлённым событием id = {}: {}", userId, eventId, eventDto);
        return service.update(userId, eventId, eventDto);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Получен запрос PATCH /users/{}/events/{eventId}/requests" +
                " на обновление статуса события id = {}: {}", userId, eventId, request);
        if (Status.from(request.getStatus()) == null) {
            throw new ConflictException("Status is not validate");
        }
        return service.updateRequestStatus(userId, eventId, request);
    }
}