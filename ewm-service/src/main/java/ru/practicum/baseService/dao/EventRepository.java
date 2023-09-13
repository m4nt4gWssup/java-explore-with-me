package ru.practicum.baseService.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.baseService.enums.State;
import ru.practicum.baseService.model.Category;
import ru.practicum.baseService.model.Event;
import ru.practicum.baseService.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, EventCriteriaRepository {

    boolean existsByCategory(Category category);

    Page<Event> findByInitiator(User initiator, Pageable page);

    Optional<Event> findByIdAndInitiatorId(Long id, Long userId);

    Set<Event> findAllByIdIn(Set<Long> ids);

    Optional<Event> findByInitiatorAndId(User initiator, Long id);

    @Query("SELECT e FROM Event e " +
            "WHERE (:users IS NULL OR e.initiator.id IN (:users)) " +
            "AND (:states IS NULL OR e.state IN (:states)) " +
            "AND (:categories IS NULL OR e.category.id IN (:categories)) " +
            "AND e.date BETWEEN :rangeStart AND :rangeEnd")
    List<Event> findEventsByParams(
            @Param("users") List<Long> users,
            @Param("states") List<State> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    boolean existsByIdAndInitiatorId(Long id, Long userId);
}