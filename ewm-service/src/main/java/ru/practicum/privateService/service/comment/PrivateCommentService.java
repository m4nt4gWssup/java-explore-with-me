package ru.practicum.privateService.service.comment;

import ru.practicum.baseService.dto.comment.CommentDto;
import ru.practicum.baseService.dto.comment.NewCommentDto;
import ru.practicum.baseService.dto.comment.UpdateCommentDto;

import java.util.List;

public interface PrivateCommentService {

    CommentDto save(Long userId, Long eventId, NewCommentDto newCommentDto);

    UpdateCommentDto update(Long userId, Long commentId, NewCommentDto newCommentDto);

    void delete(Long userId, Long commentId);

    List<CommentDto> getCommentsByUserId(Long userId);
}