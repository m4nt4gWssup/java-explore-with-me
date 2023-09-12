package ru.practicum.baseService.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.baseService.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByIdIn(List<Long> ids, Pageable pageable);
}