package ru.practicum.publicService.service.event;

import ru.practicum.baseService.dto.event.EventFullDto;
import ru.practicum.baseService.dto.event.EventShortDto;
import ru.practicum.publicService.dto.RequestParamForEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface PublicEventsService {

    Set<EventShortDto> getAll(RequestParamForEvent param);

    EventFullDto get(Long id, HttpServletRequest request);
}