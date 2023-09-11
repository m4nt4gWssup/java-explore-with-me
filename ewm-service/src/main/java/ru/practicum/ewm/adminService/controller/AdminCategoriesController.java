package ru.practicum.ewm.adminService.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminService.service.category.AdminCategoriesService;
import ru.practicum.ewm.baseService.dto.Category.CategoryDto;
import ru.practicum.ewm.baseService.dto.Category.NewCategoryDto;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    public final AdminCategoriesService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid NewCategoryDto dto) {
        log.info("Получен запрос POST /admin/categories c новой категорией: {}", dto.getName());
        return service.create(dto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        log.info("Получен запрос DELETE /admin/categories/{}", catId);
        service.delete(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@RequestBody @Valid NewCategoryDto dto,
                              @PathVariable Long catId) {
        log.info("Получен запрос PATCH /admin/categories/{} на изменение категориии: {}", catId, dto.getName());
        return service.update(dto, catId);
    }
}