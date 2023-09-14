package ru.practicum.baseService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.baseService.model.Comment;
import ru.practicum.baseService.model.Event;
import ru.practicum.baseService.model.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEvent(Event event);

    List<Comment> findAllByAuthor(User user);
}