package ru.practicum.adminService.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.baseService.dao.CommentRepository;
import ru.practicum.baseService.exception.NotFoundException;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdminCommentServiceImpl implements AdminCommentService {

    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public void deleteByAdmin(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            log.info("Ошибка при удалении комментария. Комментарий id={} не найден", commentId);
            throw new NotFoundException(String.format("Комментарий id=%d не найден", commentId));
        } else {
            log.info("Комментарий с id={} успешно удален", commentId);
            commentRepository.deleteById(commentId);
        }
    }
}