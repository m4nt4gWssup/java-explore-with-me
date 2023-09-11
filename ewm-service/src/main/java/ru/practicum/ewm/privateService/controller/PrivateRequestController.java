package ru.practicum.ewm.privateService.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.baseService.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.privateService.service.request.PrivateRequestService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {

    private final PrivateRequestService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        log.info("Получен запрос GET /users/{}/requests", userId);
        return service.getRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable Long userId,
                                          @RequestParam Long eventId) {
        log.info("Получен запрос POST /users/{}/requests c новым запросом на участие в Event с id = {}", userId, eventId);
        return service.create(userId, eventId);
    }

    @PatchMapping("/{requestsId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto update(@PathVariable Long userId, @PathVariable Long requestsId) {
        log.info("Получен запрос PATCH /users/{}/requests/{requestsId}/cancel" +
                " c отменой запроса id = {}", userId, requestsId);
        return service.update(userId, requestsId);
    }
}