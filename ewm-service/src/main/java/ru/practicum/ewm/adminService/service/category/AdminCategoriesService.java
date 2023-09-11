package ru.practicum.ewm.adminService.service.category;

import ru.practicum.ewm.baseService.dto.Category.CategoryDto;
import ru.practicum.ewm.baseService.dto.Category.NewCategoryDto;

public interface AdminCategoriesService {
    CategoryDto create(NewCategoryDto dto);

    void delete(Long catId);

    CategoryDto update(NewCategoryDto dto, Long catId);
}