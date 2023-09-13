package ru.practicum.publicService.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.baseService.dao.EventRepository;
import ru.practicum.baseService.dto.event.EventFullDto;
import ru.practicum.baseService.dto.event.EventShortDto;
import ru.practicum.baseService.enums.State;
import ru.practicum.baseService.exception.NotFoundException;
import ru.practicum.baseService.exception.ValidationException;
import ru.practicum.baseService.mapper.EventMapper;
import ru.practicum.baseService.model.Event;
import ru.practicum.baseService.model.EventSearchCriteria;
import ru.practicum.baseService.util.page.CustomPageRequest;
import ru.practicum.client.HitClient;
import ru.practicum.publicService.dto.RequestParamForEvent;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PublicEventsServiceImpl implements PublicEventsService {
    private final EventRepository eventRepository;

    private final HitClient hitClient;

    @Transactional
    @Override
    public Set<EventShortDto> getAll(RequestParamForEvent param) {
        CustomPageRequest pageable = createPageable(param.getSort(), param.getFrom(), param.getSize());
        if (param.getRangeStart() != null && param.getRangeEnd() != null) {
            if (param.getRangeStart().isAfter(param.getRangeEnd())) {
                throw new ValidationException("Неправильно указано время");
            }
        }
        EventSearchCriteria eventSearchCriteria = createCriteria(param);
        Set<EventShortDto> eventShorts = EventMapper.toEventShortDtoSet(eventRepository
                .findAllWithFilters(pageable, eventSearchCriteria).toSet());
        log.info("Получен список событий размером: {}", eventShorts.size());
        hitClient.create(param.getRequest().getRemoteAddr(), param.getRequest().getRequestURI(), LocalDateTime.now());
        return eventShorts;
    }

    @Transactional
    @Override
    public EventFullDto get(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Событие не найдено с id = %s", id)));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException(String.format("Событие с id=%d не опубликовано", id));
        }
        hitClient.create(request.getRemoteAddr(), request.getRequestURI(), LocalDateTime.now());
        try {
            event.setViews(hitClient.getStatistic(
                    "2000-09-01 00:00:00",
                    "2032-09-30 00:00:00",
                    Collections.singletonList(request.getRequestURI()),
                    true).get(0).getHits());
        } catch (RuntimeException e) {
            throw new ValidationException("Ошибка:" + e.getMessage());
        }
        return EventMapper.toEventFullDto(event);
    }

    private CustomPageRequest createPageable(String sort, int from, int size) {
        CustomPageRequest pageable = null;
        if (sort == null || sort.equalsIgnoreCase("EVENT_DATE")) {
            pageable = new CustomPageRequest(from, size,
                    Sort.by(Sort.Direction.ASC, "event_date"));
        } else if (sort.equalsIgnoreCase("VIEWS")) {
            pageable = new CustomPageRequest(from, size,
                    Sort.by(Sort.Direction.ASC, "views"));
        }
        return pageable;
    }

    private EventSearchCriteria createCriteria(RequestParamForEvent param) {
        return EventSearchCriteria.builder()
                .text(param.getText())
                .categories(param.getCategories())
                .rangeEnd(param.getRangeEnd())
                .rangeStart(param.getRangeStart())
                .paid(param.getPaid())
                .build();
    }
}