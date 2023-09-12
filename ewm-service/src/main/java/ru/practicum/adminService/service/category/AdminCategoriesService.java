package ru.practicum.adminService.service.category;

import ru.practicum.baseService.dto.Category.CategoryDto;
import ru.practicum.baseService.dto.Category.NewCategoryDto;

public interface AdminCategoriesService {
    CategoryDto create(NewCategoryDto dto);

    void delete(Long catId);

    CategoryDto update(NewCategoryDto dto, Long catId);
}