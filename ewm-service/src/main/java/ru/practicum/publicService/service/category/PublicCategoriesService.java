package ru.practicum.publicService.service.category;

import ru.practicum.baseService.dto.Category.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {
    List<CategoryDto> getAll(int from, int size);

    CategoryDto get(Long catId);
}