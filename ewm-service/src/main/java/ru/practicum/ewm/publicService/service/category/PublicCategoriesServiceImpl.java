package ru.practicum.ewm.publicService.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.baseService.dao.CategoriesRepository;
import ru.practicum.ewm.baseService.dto.Category.CategoryDto;
import ru.practicum.ewm.baseService.exception.NotFoundException;
import ru.practicum.ewm.baseService.mapper.CategoryMapper;
import ru.practicum.ewm.baseService.model.Category;
import ru.practicum.ewm.baseService.util.page.CustomPageRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PublicCategoriesServiceImpl implements PublicCategoriesService {

    private final CategoriesRepository categoriesRepository;

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        CustomPageRequest pageable = new CustomPageRequest(from, size,
                Sort.by(Sort.Direction.ASC, "id"));
        List<Category> categories = categoriesRepository.findAll(pageable).toList();
        log.info("Получен список всех категорий");
        return CategoryMapper.toDtoList(categories);
    }

    @Override
    public CategoryDto get(Long catId) {
        final Category category = categoriesRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория не найдена с id = %s", catId)));
        log.info("Получена категория: {}", category.getName());
        return CategoryMapper.toDto(category);
    }
}