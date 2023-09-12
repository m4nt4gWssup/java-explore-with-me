package ru.practicum.privateService.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.baseService.dao.EventRepository;
import ru.practicum.baseService.dao.RequestRepository;
import ru.practicum.baseService.dao.UserRepository;
import ru.practicum.baseService.dto.request.ParticipationRequestDto;
import ru.practicum.baseService.enums.State;
import ru.practicum.baseService.enums.Status;
import ru.practicum.baseService.exception.ConflictException;
import ru.practicum.baseService.exception.NotFoundException;
import ru.practicum.baseService.mapper.RequestMapper;
import ru.practicum.baseService.model.Event;
import ru.practicum.baseService.model.Request;
import ru.practicum.baseService.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PrivateRequestServiceImpl implements PrivateRequestService {

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        if (userRepository.existsById(userId)) {
            return RequestMapper.toDtoList(requestRepository.findAllByRequesterId(userId));
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %s не найден", userId));
        }
    }

    @Transactional
    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id = %s не найдено", eventId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %s не найден", userId)));
        Request userRequest = new Request();
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException(String.format("Запрос с requesterId=%d и eventId=%d уже существует", userId, eventId));
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException(String.format("Пользователь с id=%d не должен совпадать с инициатором", userId));
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException(String.format("Событие с id=%d не опубликовано", eventId));
        }
        if (event.getParticipantLimit() == 0) {
            userRequest.setCreated(LocalDateTime.now());
            userRequest.setEvent(event);
            userRequest.setRequester(userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Такого пользователя не существует!")));
            userRequest.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else if (!event.getRequestModeration()) {
            if (!(event.getConfirmedRequests() < event.getParticipantLimit()) ||
                    event.getParticipantLimit().equals(event.getConfirmedRequests())) {
                throw new ConflictException("Достигнут лимит запросов на участие!");
            }
            userRequest.setCreated(LocalDateTime.now());
            userRequest.setEvent(event);
            userRequest.setRequester(userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Такого пользователя не существует!")));
            userRequest.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            if (!(event.getConfirmedRequests() < event.getParticipantLimit()) ||
                    event.getParticipantLimit().equals(event.getConfirmedRequests())) {
                throw new ConflictException("Достигнут лимит запросов на участие!");
            }
            userRequest.setCreated(LocalDateTime.now());
            userRequest.setEvent(event);
            userRequest.setRequester(userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Такого пользователя не существует!")));
            userRequest.setStatus(Status.PENDING);
            eventRepository.save(event);
        }
        return RequestMapper.toParticipationRequestDto(requestRepository.save(RequestMapper.toRequest(event, user)));
    }

    @Transactional
    @Override
    public ParticipationRequestDto update(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id=%d " +
                        "и requesterId=%d не найден", requestId, userId)));
        request.setStatus(Status.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }
}