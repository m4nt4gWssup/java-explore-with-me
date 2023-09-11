package ru.practicum.ewm.baseService.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.baseService.model.Event;
import ru.practicum.ewm.baseService.model.EventSearchCriteria;

@Repository
public interface EventCriteriaRepository {

    Page<Event> findAllWithFilters(Pageable pageable, EventSearchCriteria eventSearchCriteria);
}