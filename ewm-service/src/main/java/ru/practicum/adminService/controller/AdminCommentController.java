package ru.practicum.adminService.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.adminService.service.comment.AdminCommentService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/comments")
public class AdminCommentController {

    private final AdminCommentService commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        log.info("Удаление комментария");
        commentService.deleteByAdmin(commentId);
    }
}