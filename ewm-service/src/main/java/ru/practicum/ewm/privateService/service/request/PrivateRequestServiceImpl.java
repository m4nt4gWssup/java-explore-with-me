package ru.practicum.ewm.privateService.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.baseService.dao.EventRepository;
import ru.practicum.ewm.baseService.dao.RequestRepository;
import ru.practicum.ewm.baseService.dao.UserRepository;
import ru.practicum.ewm.baseService.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.baseService.enums.State;
import ru.practicum.ewm.baseService.enums.Status;
import ru.practicum.ewm.baseService.exception.ConflictException;
import ru.practicum.ewm.baseService.exception.NotFoundException;
import ru.practicum.ewm.baseService.mapper.RequestMapper;
import ru.practicum.ewm.baseService.model.Event;
import ru.practicum.ewm.baseService.model.Request;
import ru.practicum.ewm.baseService.model.User;

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
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id = %s не найдено", eventId)));
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %s не найден", userId)));

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException(String.format("Запрос с requesterId=%d и eventId=%d уже существует", userId, eventId));
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException(String.format("Пользователь с id=%d не должен совпадать с инициатором", userId));
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException(String.format("Событие с id=%d не опубликовано", eventId));
        }
        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ConflictException(String.format("Событие с id=%d достигло лимита участников", eventId));
        }
        if (!event.getRequestModeration()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
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