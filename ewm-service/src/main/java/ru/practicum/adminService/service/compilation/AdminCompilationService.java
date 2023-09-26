package ru.practicum.adminService.service.compilation;

import ru.practicum.baseService.dto.compilation.CompilationDto;
import ru.practicum.baseService.dto.compilation.NewCompilationDto;
import ru.practicum.baseService.dto.compilation.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);
}