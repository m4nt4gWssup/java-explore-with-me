package ru.practicum.ewm.baseService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.baseService.model.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
}