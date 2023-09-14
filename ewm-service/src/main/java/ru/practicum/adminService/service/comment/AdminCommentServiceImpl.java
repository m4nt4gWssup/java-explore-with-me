package ru.practicum.adminService.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.baseService.dao.CommentRepository;
import ru.practicum.baseService.exception.NotFoundException;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdminCommentServiceImpl implements AdminCommentService {

    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public void deleteByAdmin(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException(String.format("Комментарий id=%d не найден", commentId));
        } else {
            commentRepository.deleteById(commentId);
        }
    }
}