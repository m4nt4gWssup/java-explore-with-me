package ru.practicum.ewm.publicService.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.baseService.dto.Category.CategoryDto;
import ru.practicum.ewm.publicService.service.category.PublicCategoriesService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/categories")
public class PublicCategoriesController {

    public final PublicCategoriesService categoriesService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /categories c параметрами: from = {}, size = {}", from, size);
        return categoriesService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto get(@PathVariable Long catId) {
        log.info("Получен запрос GET /categories/{}", catId);
        return categoriesService.get(catId);
    }
}