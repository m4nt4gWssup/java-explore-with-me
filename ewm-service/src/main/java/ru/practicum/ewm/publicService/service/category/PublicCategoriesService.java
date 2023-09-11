package ru.practicum.ewm.publicService.service.category;

import ru.practicum.ewm.baseService.dto.Category.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {
    List<CategoryDto> getAll(int from, int size);

    CategoryDto get(Long catId);
}