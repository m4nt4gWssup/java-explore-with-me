package ru.practicum.privateService.service.request;

import ru.practicum.baseService.dto.request.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {
    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto update(Long userId, Long requestsId);
}