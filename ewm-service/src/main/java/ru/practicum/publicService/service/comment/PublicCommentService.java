package ru.practicum.publicService.service.comment;

import ru.practicum.baseService.dto.comment.CommentDto;

import java.util.List;

public interface PublicCommentService {

    List<CommentDto> getAll(Long eventId);
}