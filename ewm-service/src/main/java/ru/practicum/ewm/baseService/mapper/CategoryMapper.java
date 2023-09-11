package ru.practicum.ewm.baseService.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.baseService.dto.Category.CategoryDto;
import ru.practicum.ewm.baseService.dto.Category.NewCategoryDto;
import ru.practicum.ewm.baseService.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class CategoryMapper {

    public Category toEntity(NewCategoryDto dto) {
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