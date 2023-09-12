package ru.practicum.baseService.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.baseService.dto.request.ParticipationRequestDto;
import ru.practicum.baseService.model.Event;
import ru.practicum.baseService.model.Request;
import ru.practicum.baseService.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.baseService.enums.Status.CONFIRMED;
import static ru.practicum.baseService.enums.Status.PENDING;


@UtilityClass
public final class RequestMapper {

    public Request toRequest(Event event, User requester) {
        return Request.builder()
                .requester(requester)
                .event(event)
                .created(LocalDateTime.now())
                .status(event.getRequestModeration() ? PENDING : CONFIRMED)
                .build();
    }

    public ParticipationRequestDto toParticipationRequestDto(Request entity) {
        return ParticipationRequestDto.builder()
                .id(entity.getId())
                .requester(entity.getRequester().getId())
                .created(entity.getCreated())
                .event(entity.getEvent().getId())
                .status(entity.getStatus())
                .build();
    }

    public List<ParticipationRequestDto> toDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }
}