package ru.practicum.baseService.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.baseService.dto.comment.CommentDto;
import ru.practicum.baseService.dto.comment.NewCommentDto;
import ru.practicum.baseService.dto.comment.UpdateCommentDto;
import ru.practicum.baseService.model.Comment;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment toComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .created(LocalDateTime.now())
                .build();
    }

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .created(comment.getCreated())
                .build();
    }

    public UpdateCommentDto toUpdateDto(Comment comment) {
        return UpdateCommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .created(comment.getCreated())
                .edited(LocalDateTime.now())
                .build();
    }
}