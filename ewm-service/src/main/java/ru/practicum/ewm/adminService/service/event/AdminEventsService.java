package ru.practicum.ewm.adminService.service.event;

import ru.practicum.ewm.adminService.dto.RequestParamForEvent;
import ru.practicum.ewm.baseService.dto.event.EventFullDto;
import ru.practicum.ewm.baseService.dto.event.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventsService {
    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent);

    List<EventFullDto> getAll(RequestParamForEvent param);
}