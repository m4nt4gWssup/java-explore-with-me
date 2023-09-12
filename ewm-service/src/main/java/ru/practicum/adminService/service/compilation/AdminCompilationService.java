package ru.practicum.adminService.service.compilation;

import ru.practicum.baseService.dto.Compilation.CompilationDto;
import ru.practicum.baseService.dto.Compilation.NewCompilationDto;
import ru.practicum.baseService.dto.Compilation.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);
}