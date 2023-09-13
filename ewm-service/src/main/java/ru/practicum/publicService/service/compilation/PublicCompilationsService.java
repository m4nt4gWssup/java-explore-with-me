package ru.practicum.publicService.service.compilation;

import ru.practicum.baseService.dto.Compilation.CompilationDto;

import java.util.List;

public interface PublicCompilationsService {
    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto get(Long comId);
}