package ru.practicum.ewm.privateService.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.baseService.dao.CategoriesRepository;
import ru.practicum.ewm.baseService.dao.EventRepository;
import ru.practicum.ewm.baseService.dao.RequestRepository;
import ru.practicum.ewm.baseService.dao.UserRepository;
import ru.practicum.ewm.baseService.dto.event.*;
import ru.practicum.ewm.baseService.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.baseService.enums.State;
import ru.practicum.ewm.baseService.enums.UserStateAction;
import ru.practicum.ewm.baseService.exception.ConflictException;
import ru.practicum.ewm.baseService.exception.NotFoundException;
import ru.practicum.ewm.baseService.mapper.EventMapper;
import ru.practicum.ewm.baseService.mapper.RequestMapper;
import ru.practicum.ewm.baseService.model.Event;
import ru.practicum.ewm.baseService.model.Request;
import ru.practicum.ewm.baseService.util.UtilMergeProperty;
import ru.practicum.ewm.baseService.util.page.CustomPageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.ewm.baseService.enums.Status.CONFIRMED;
import static ru.practicum.ewm.baseService.enums.Status.REJECTED;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PrivateEventsServiceImpl implements PrivateEventsService {
    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private final CategoriesRepository categoriesRepository;

    @Override
    public Set<EventShortDto> getAll(Long userId, Integer from, Integer size) {
        CustomPageRequest pageRequest = new CustomPageRequest(from, size,
                Sort.by(Sort.Direction.ASC, "id"));
        Set<EventShortDto> eventShorts = EventMapper.toEventShortDtoList(eventRepository.findAll(pageRequest).toSet());
        log.info("Получен список событий размером: {}", eventShorts.size());
        return eventShorts;
    }

    @Override
    public EventFullDto get(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Событие не найдено с id = %s и userId = %s", eventId, userId)));
        log.info("Получено событие: {}", event.getId());
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new NotFoundException(
                    String.format("Событие не найдено с id = %s и userId = %s", eventId, userId));
        }
        return RequestMapper.toDtoList(requestRepository.findAllByEventId(eventId));
    }

    @Transactional
    @Override
    public EventFullDto create(Long userId, NewEventDto eventDto) {
        checkEventDate(eventDto.getEventDate());
        Event event = EventMapper.toEntity(eventDto);
        event.setCategory(categoriesRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Категория с id=%d не найдена",
                        eventDto.getCategory()))));
        event.setPublishedOn(LocalDateTime.now());
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%d не найден", userId))));
        event.setViews(0L);
        try {
            event = eventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Добавлено событие: {}", event.getTitle());
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest eventDto) {
        Event eventTarget = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Событие не найдено с id = %s и userId = %s", eventId, userId)));
        Event eventUpdate = EventMapper.toEntity(eventDto);
        checkEventDate(eventUpdate.getDate());

        if (eventDto.getCategory() != null) {
            eventUpdate.setCategory(categoriesRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Категория с id=%d не найдена",
                            eventDto.getCategory()))));
        }

        if (eventTarget.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Событие не должно быть опубликовано");
        }

        UtilMergeProperty.copyProperties(eventUpdate, eventTarget);
        if (UserStateAction.CANCEL_REVIEW.toString().equals(eventDto.getStateAction().toString())) {
            eventTarget.setState(State.CANCELED);
        } else if (UserStateAction.SEND_TO_REVIEW.toString().equals(eventDto.getStateAction().toString())) {
            eventTarget.setState(State.PENDING);
        }

        eventRepository.flush();
        log.info("Обновлено событие: {}", eventTarget.getTitle());
        return EventMapper.toEventFullDto(eventTarget);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        List<ParticipationRequestDto> confirmedRequests = List.of();
        List<ParticipationRequestDto> rejectedRequests = List.of();

        List<Long> requestIds = request.getRequestIds();
        List<Request> requests = requestRepository.findAllByIdIn(requestIds);

        String status = request.getStatus();

        if (status.equals(REJECTED.toString())) {
            if (status.equals(REJECTED.toString())) {
                boolean isConfirmedRequestExists = requests.stream()
                        .anyMatch(r -> r.getStatus().equals(CONFIRMED));
                if (isConfirmedRequestExists) {
                    throw new ConflictException("Нельзя отклонить подтвержденные запросы");
                }
                rejectedRequests = requests.stream()
                        .peek(r -> r.setStatus(REJECTED))
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
            }
        }

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Событие не найдено с id = %s и userId = %s", eventId, userId)));
        Long participantLimit = event.getParticipantLimit();
        Long approvedRequests = event.getConfirmedRequests();
        long availableParticipants = participantLimit - approvedRequests;
        long potentialParticipants = requestIds.size();

        if (participantLimit > 0 && participantLimit.equals(approvedRequests)) {
            throw new ConflictException(String.format("Событие с id=%d достигло лимита участников", eventId));
        }

        if (status.equals(CONFIRMED.toString())) {
            if (participantLimit.equals(0L) || (potentialParticipants <= availableParticipants && !event.getRequestModeration())) {
                confirmedRequests = requests.stream()
                        .peek(r -> {
                            if (!r.getStatus().equals(CONFIRMED)) {
                                r.setStatus(CONFIRMED);
                            } else {
                                throw new ConflictException(String.format("Запрос с id=%d уже был подтвержден", r.getId()));
                            }
                        })
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(approvedRequests + potentialParticipants);
            } else {
                confirmedRequests = requests.stream()
                        .limit(availableParticipants)
                        .peek(r -> {
                            if (!r.getStatus().equals(CONFIRMED)) {
                                r.setStatus(CONFIRMED);
                            } else {
                                throw new ConflictException(String.format("Запрос с id=%d уже был подтвержден", r.getId()));
                            }
                        })
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                rejectedRequests = requests.stream()
                        .skip(availableParticipants)
                        .peek(r -> {
                            if (!r.getStatus().equals(REJECTED)) {
                                r.setStatus(REJECTED);
                            } else {
                                throw new ConflictException(String.format("Запрос с id=%d уже был отклонен", r.getId()));
                            }
                        })
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(participantLimit);
            }
        }
        eventRepository.flush();
        requestRepository.flush();
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Поле: eventDate. Ошибка: дата и время, на которые запланировано событие," +
                    " не могут быть раньше чем через два часа от текущего момента. Значение: " + eventDate);
        }
    }
}