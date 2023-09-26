package ru.practicum.publicService.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.baseService.dto.comment.CommentDto;
import ru.practicum.publicService.service.comment.PublicCommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicCommentController {

    private final PublicCommentService commentService;

    @GetMapping("events/{eventId}/comments")
    public List<CommentDto> getComments(@PathVariable Long eventId) {
        log.info("Получен запрос GET /comments/{}", eventId);
        return commentService.getAll(eventId);
    }
}