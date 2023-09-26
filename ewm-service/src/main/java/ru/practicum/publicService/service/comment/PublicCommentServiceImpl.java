package ru.practicum.publicService.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.baseService.dao.CommentRepository;
import ru.practicum.baseService.dao.EventRepository;
import ru.practicum.baseService.dto.comment.CommentDto;
import ru.practicum.baseService.exception.NotFoundException;
import ru.practicum.baseService.mapper.CommentMapper;
import ru.practicum.baseService.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PublicCommentServiceImpl implements PublicCommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDto> getAll(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                String.format("Событие id=%d не найдено", eventId)));
        log.info("Получение всех комментариев для события с id={}", eventId);
        return commentRepository.findAllByEvent(event).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }
}