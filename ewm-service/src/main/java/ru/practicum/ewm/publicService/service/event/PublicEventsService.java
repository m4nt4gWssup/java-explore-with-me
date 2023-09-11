package ru.practicum.ewm.publicService.service.event;

import ru.practicum.ewm.baseService.dto.event.EventFullDto;
import ru.practicum.ewm.baseService.dto.event.EventShortDto;
import ru.practicum.ewm.publicService.dto.RequestParamForEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface PublicEventsService {

    Set<EventShortDto> getAll(RequestParamForEvent param);

    EventFullDto get(Long id, HttpServletRequest request);
}