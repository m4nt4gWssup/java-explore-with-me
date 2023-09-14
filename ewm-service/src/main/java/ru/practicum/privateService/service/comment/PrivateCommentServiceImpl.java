package ru.practicum.privateService.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.baseService.dao.CommentRepository;
import ru.practicum.baseService.dao.EventRepository;
import ru.practicum.baseService.dao.UserRepository;
import ru.practicum.baseService.dto.comment.CommentDto;
import ru.practicum.baseService.dto.comment.NewCommentDto;
import ru.practicum.baseService.dto.comment.UpdateCommentDto;
import ru.practicum.baseService.exception.CommentUpdateException;
import ru.practicum.baseService.exception.NotFoundException;
import ru.practicum.baseService.mapper.CommentMapper;
import ru.practicum.baseService.model.Comment;
import ru.practicum.baseService.model.Event;
import ru.practicum.baseService.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PrivateCommentServiceImpl implements PrivateCommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentDto save(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User author = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("Пользователь id=%d не найден", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                String.format("Событие с id=%d не найдено", eventId)));
        Comment comment = CommentMapper.toComment(newCommentDto);
        comment.setAuthor(author);
        comment.setEvent(event);
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public UpdateCommentDto update(Long userId, Long commentId, NewCommentDto newCommentDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь id=%d не найден", userId));
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("Комментарий с id=%d не найден", commentId)));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new CommentUpdateException(String.format(
                    "Пользователь id=%d не является автором комментария с id=%d", userId, commentId));
        }
        if (LocalDateTime.now().isAfter(comment.getCreated().plusHours(1))) {
            throw new CommentUpdateException("С момента создания комментария прошло больше часа, " +
                    "редактирование не возможно");
        }
        comment.setText(newCommentDto.getText());
        return CommentMapper.toUpdateDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByUserId(Long userId) {
        User author = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("Пользователь id=%d не найден", userId)));
        return commentRepository.findAllByAuthor(author).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long userId, Long commentId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь id=%d не найден", userId));
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("Комментарий с id=%d не найден", commentId)));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new CommentUpdateException(String.format(
                    "Пользователь id=%d не является автором комментария с id=%d", userId, commentId));
        }
        commentRepository.deleteById(commentId);
    }
}