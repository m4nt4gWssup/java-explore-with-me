package ru.practicum.baseService.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.baseService.dto.request.ParticipationRequestDto;
import ru.practicum.baseService.model.Request;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public final class RequestMapper {

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