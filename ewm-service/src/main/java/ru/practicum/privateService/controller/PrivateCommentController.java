package ru.practicum.privateService.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.baseService.dto.comment.CommentDto;
import ru.practicum.baseService.dto.comment.NewCommentDto;
import ru.practicum.baseService.dto.comment.UpdateCommentDto;
import ru.practicum.privateService.service.comment.PrivateCommentService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/comments")
@Validated
public class PrivateCommentController {

    private final PrivateCommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAll(@PathVariable Long userId) {
        log.info("Получен запрос GET /users/{}/comments}", userId);
        return new ResponseEntity<>(commentService.getCommentsByUserId(userId), HttpStatus.OK);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<CommentDto> save(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Создание комментария");
        return new ResponseEntity<>(commentService.save(userId, eventId, newCommentDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<UpdateCommentDto> update(@PathVariable Long userId,
                                                   @PathVariable Long commentId,
                                                   @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Обновление комментария");
        return new ResponseEntity<>(commentService.update(userId, commentId, newCommentDto), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId,
                       @PathVariable Long commentId) {
        log.info("Удаление комментария");
        commentService.delete(userId, commentId);
    }
}