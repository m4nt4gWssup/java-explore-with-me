package ru.practicum.baseService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.baseService.model.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
}