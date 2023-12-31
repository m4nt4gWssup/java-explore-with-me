package ru.practicum.adminService.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.baseService.dao.CategoriesRepository;
import ru.practicum.baseService.dao.EventRepository;
import ru.practicum.baseService.dto.category.CategoryDto;
import ru.practicum.baseService.dto.category.NewCategoryDto;
import ru.practicum.baseService.exception.ConditionsNotMetException;
import ru.practicum.baseService.exception.ConflictException;
import ru.practicum.baseService.exception.NotFoundException;
import ru.practicum.baseService.mapper.CategoryMapper;
import ru.practicum.baseService.model.Category;
import ru.practicum.baseService.util.UtilMergeProperty;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminCategoriesServiceImpl implements AdminCategoriesService {

    private final CategoriesRepository categoriesRepository;

    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto dto) {
        Category category = CategoryMapper.toCategory(dto);
        try {
            category = categoriesRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка при создании категории: {}", e.getMessage(), e);
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Добавлена категория: {}", category.getName());
        return CategoryMapper.toDto(category);
    }

    @Transactional
    @Override
    public void delete(Long catId) {
        if (eventRepository.existsByCategory(get(catId))) {
            throw new ConditionsNotMetException("Категория не пуста");
        } else {
            log.info("Удалена категория с id = {}", catId);
            categoriesRepository.deleteById(catId);
        }
    }

    @Transactional
    @Override
    public CategoryDto update(NewCategoryDto dto, Long catId) {
        Category categoryUpdate = CategoryMapper.toCategory(dto);
        Category categoryTarget = get(catId);
        try {
            UtilMergeProperty.copyProperties(categoryUpdate, categoryTarget);
            categoriesRepository.flush();
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка при обновлении категории: {}", e.getMessage(), e);
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Обновлена категория: {}", categoryTarget.getName());
        return CategoryMapper.toDto(categoryTarget);
    }

    private Category get(Long id) {
        final Category category = categoriesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Категория с id = %s не найдена", id)));
        log.info("Получена категория: {}", category.getName());
        return category;
    }
}