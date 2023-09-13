package ru.practicum.adminService.service.event;

import ru.practicum.adminService.dto.RequestParamForEvent;
import ru.practicum.baseService.dto.event.EventFullDto;
import ru.practicum.baseService.dto.event.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventsService {
    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent);

    List<EventFullDto> getAll(RequestParamForEvent param);

    RequestParamForEvent buildRequestParamForEvent(List<Long> users,
                                                   List<String> states,
                                                   List<Long> categories,
                                                   LocalDateTime rangeStart,
                                                   LocalDateTime rangeEnd,
                                                   int from,
                                                   int size);
}