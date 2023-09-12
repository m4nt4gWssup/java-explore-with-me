package ru.practicum.adminService.service.event;

import ru.practicum.adminService.dto.RequestParamForEvent;
import ru.practicum.baseService.dto.event.EventFullDto;
import ru.practicum.baseService.dto.event.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventsService {
    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent);

    List<EventFullDto> getAll(RequestParamForEvent param);
}