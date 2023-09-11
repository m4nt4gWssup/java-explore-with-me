package ru.practicum.ewm.adminService.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminService.service.compilation.AdminCompilationService;
import ru.practicum.ewm.baseService.dto.Compilation.CompilationDto;
import ru.practicum.ewm.baseService.dto.Compilation.NewCompilationDto;
import ru.practicum.ewm.baseService.dto.Compilation.UpdateCompilationRequest;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {
    private final AdminCompilationService service;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto save(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Получен запрос POST /admin/compilations c новой подборкой: {}", newCompilationDto.getTitle());
        return service.save(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        log.info("Получен запрос DELETE /admin/compilations/{}", compId);
        service.delete(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto update(@PathVariable Long compId,
                                 @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Получен запрос PATCH /admin/compilations/{} на изменение подборки.", compId);
        return service.update(compId, updateCompilationRequest);
    }
}