package ru.practicum.baseService.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.baseService.dto.category.CategoryDto;
import ru.practicum.baseService.dto.category.NewCategoryDto;
import ru.practicum.baseService.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class CategoryMapper {

    public Category toCategory(NewCategoryDto dto) {
        return Category.builder()
                .name(dto.getName()).build();
    }

    public CategoryDto toDto(Category entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public List<CategoryDto> toDtoList(List<Category> categories) {
        return categories.stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }
}