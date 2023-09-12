package ru.practicum.ewm.publicService.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitClient;
import ru.practicum.dto.HitDto;
import ru.practicum.ewm.baseService.dao.EventCriteriaRepository;
import ru.practicum.ewm.baseService.dao.EventRepository;
import ru.practicum.ewm.baseService.dto.event.EventFullDto;
import ru.practicum.ewm.baseService.dto.event.EventShortDto;
import ru.practicum.ewm.baseService.enums.State;
import ru.practicum.ewm.baseService.exception.NotFoundException;
import ru.practicum.ewm.baseService.mapper.EventMapper;
import ru.practicum.ewm.baseService.model.Event;
import ru.practicum.ewm.baseService.model.EventSearchCriteria;
import ru.practicum.ewm.baseService.util.page.CustomPageRequest;
import ru.practicum.ewm.publicService.dto.RequestParamForEvent;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PublicEventsServiceImpl implements PublicEventsService {

    private final EventRepository eventRepository;

    private final HitClient statsClient;

    @Value("${ewm.service.name}")
    private final String serviceName;

    @Transactional
    @Override
    public Set<EventShortDto> getAll(RequestParamForEvent param) {
        CustomPageRequest pageable = createPageable(param.getSort(), param.getFrom(), param.getSize());
        EventSearchCriteria eventSearchCriteria = createCriteria(param);
        Set<EventShortDto> eventShorts = EventMapper.toEventShortDtoList(eventRepository
                .findAllWithFilters(pageable, eventSearchCriteria).toSet());
        log.info("Получен список событий размером: {}", eventShorts.size());
        saveEndpointHit(param.getRequest());
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
        saveEndpointHit(request);
        log.info("Получено событие: {}", event.getId());
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
        return EventMapper.toEventFullDto(event);
    }

    private void saveEndpointHit(HttpServletRequest request) {

        HitDto endpointHit = HitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app(serviceName)
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.save(endpointHit);
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