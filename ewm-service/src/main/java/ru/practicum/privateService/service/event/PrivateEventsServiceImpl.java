package ru.practicum.privateService.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.baseService.dao.CategoriesRepository;
import ru.practicum.baseService.dao.EventRepository;
import ru.practicum.baseService.dao.RequestRepository;
import ru.practicum.baseService.dao.UserRepository;
import ru.practicum.baseService.dto.event.*;
import ru.practicum.baseService.dto.request.ParticipationRequestDto;
import ru.practicum.baseService.enums.State;
import ru.practicum.baseService.enums.UserStateAction;
import ru.practicum.baseService.exception.ConflictException;
import ru.practicum.baseService.exception.NotFoundException;
import ru.practicum.baseService.exception.ValidationException;
import ru.practicum.baseService.mapper.EventMapper;
import ru.practicum.baseService.mapper.RequestMapper;
import ru.practicum.baseService.model.Event;
import ru.practicum.baseService.model.Request;
import ru.practicum.baseService.util.page.CustomPageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.baseService.enums.Status.CONFIRMED;
import static ru.practicum.baseService.enums.Status.REJECTED;

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
        Set<Event> newEvent = eventRepository.findAll(pageRequest).toSet();
        for (Event event : newEvent) {
            event.setRequestModeration(true);
        }
        Set<EventShortDto> eventShorts = EventMapper.toEventShortDtoList(newEvent);
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
        if (event.getPaid() == null) {
            event.setPaid(false);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0L);
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
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
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest event) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        if (eventRepository.findByInitiatorAndId(userRepository.findById(userId).get(), eventId).isEmpty()) {
            throw new ConflictException("Данный пользователь не создатель события");
        }
        Event newEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такого события нет"));
        if (newEvent.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Событие уже опубликовано!");
        }
        if (event.getAnnotation() != null) {
            if (event.getAnnotation() == null || event.getAnnotation().trim().isEmpty()
                    || event.getAnnotation().length() < 20 || event.getAnnotation().length() > 2000) {
                throw new ValidationException("Аннотация не должна быть: \n" +
                        "- пустой \n" +
                        "- меньше 20 символов или больше 2000");
            }
            newEvent.setAnnotation(event.getAnnotation());
        }
        if (event.getCategory() != null) {
            newEvent.setCategory(categoriesRepository.findById(event.getCategory())
                    .orElseThrow(() -> new NotFoundException("Такой категории не существует")));
        }
        if (event.getDescription() != null) {
            if (event.getDescription() == null || event.getDescription().trim().isEmpty()
                    || event.getDescription().length() < 20 || event.getDescription().length() > 7000) {
                throw new ValidationException("Описание не должно быть пустым или быть меньше 20 или " +
                        "быть больше 7000 символов!");
            }
            newEvent.setDescription(event.getDescription());
        }
        if (event.getEventDate() != null) {
            if (event.getEventDate().isBefore(newEvent.getDate()) ||
                    event.getEventDate().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Невозможно установить такую дату");
            }
            newEvent.setDate(event.getEventDate());
        }
        if (event.getLocation() != null) {
            newEvent.getLocation().setLat(event.getLocation().getLat());
            newEvent.getLocation().setLon(event.getLocation().getLon());
        }
        if (event.getPaid() != null) {
            newEvent.setPaid(event.getPaid());
        }
        if (event.getParticipantLimit() != null) {
            newEvent.setParticipantLimit(event.getParticipantLimit());
        }
        if (event.getRequestModeration() != null) {
            newEvent.setRequestModeration(event.getRequestModeration());
        }
        if (event.getTitle() != null) {
            if (event.getTitle().length() < 3 || event.getTitle().length() > 120) {
                throw new ValidationException("Не валидная длина заголовка");
            }
            newEvent.setTitle(event.getTitle());
        }
        if (event.getStateAction() != null) {
            if (UserStateAction.CANCEL_REVIEW.toString().equals(event.getStateAction().toString())) {
                newEvent.setState(State.CANCELED);
            } else if (UserStateAction.SEND_TO_REVIEW.toString().equals(event.getStateAction().toString())) {
                newEvent.setState(State.PENDING);
            }
        }
        return EventMapper.toEventFullDto(eventRepository.save(newEvent));
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
                event.setConfirmedRequests(confirmedRequests.size());
            }
        }
        eventRepository.flush();
        requestRepository.flush();
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Поле: eventDate. Ошибка: дата и время, на которые запланировано событие," +
                    " не могут быть раньше чем через два часа от текущего момента. Значение: " + eventDate);
        }
    }
}