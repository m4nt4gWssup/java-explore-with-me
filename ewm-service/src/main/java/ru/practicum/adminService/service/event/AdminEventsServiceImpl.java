package ru.practicum.adminService.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.adminService.dto.RequestParamForEvent;
import ru.practicum.baseService.dao.EventRepository;
import ru.practicum.baseService.dto.event.EventFullDto;
import ru.practicum.baseService.dto.event.UpdateEventAdminRequest;
import ru.practicum.baseService.enums.AdminStateAction;
import ru.practicum.baseService.enums.State;
import ru.practicum.baseService.exception.ConflictException;
import ru.practicum.baseService.exception.NotFoundException;
import ru.practicum.baseService.exception.ValidationException;
import ru.practicum.baseService.mapper.EventMapper;
import ru.practicum.baseService.model.Event;
import ru.practicum.baseService.util.UtilMergeProperty;
import ru.practicum.baseService.util.page.CustomPageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminEventsServiceImpl implements AdminEventsService {

    private final EventRepository eventRepository;

    @Transactional
    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest dto) {
        if (dto.getEventDate() != null) {
            checkEventDate(dto.getEventDate());
        }
        Event eventUpdate = EventMapper.toEntity(dto);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id = %s не найдено", eventId)));
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Невозможно опубликовать событие, так как оно находится " +
                    "в неправильном статусе: PUBLISHED");
        } else if (event.getState().equals(State.CANCELED)) {
            throw new ConflictException("Невозможно опубликовать событие, так как оно находится " +
                    "в неправильном статусе: CANCELED");
        } else {
            if (dto.getStateAction().toString().equals(AdminStateAction.PUBLISH_EVENT.toString())) {
                event.setState(State.PUBLISHED);
            }
            if (dto.getStateAction().toString().equals(AdminStateAction.REJECT_EVENT.toString())) {
                event.setState(State.CANCELED);
            }
        }
        UtilMergeProperty.copyProperties(eventUpdate, event);
        try {
            eventRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Обновлено событие: {}", event.getTitle());
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventFullDto> getAll(RequestParamForEvent param) {
        CustomPageRequest pageable = new CustomPageRequest(param.getFrom(), param.getSize(),
                Sort.by(Sort.Direction.ASC, "id"));
        List<Event> events = eventRepository.findEventsByParams(
                param.getUsers(), param.getStates(), param.getCategories(), param.getRangeStart(),
                param.getRangeEnd(), pageable);
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Поле: eventDate. Ошибка: дата и время проведения " +
                    "события не могут быть раньше чем через два часа от текущего момента. Значение: " + eventDate);
        }
    }
}