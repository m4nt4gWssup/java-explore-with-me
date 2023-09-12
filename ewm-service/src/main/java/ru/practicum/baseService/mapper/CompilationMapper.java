package ru.practicum.baseService.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.baseService.dto.Compilation.CompilationDto;
import ru.practicum.baseService.dto.Compilation.NewCompilationDto;
import ru.practicum.baseService.model.Compilation;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto dto) {
        return Compilation.builder()
                .pinned(dto.isPinned())
                .title(dto.getTitle())
                .build();
    }

    public CompilationDto toDto(Compilation entity) {
        return CompilationDto.builder()
                .id(entity.getId())
                .pinned(entity.isPinned())
                .title(entity.getTitle())
                // TODO
                .events(EventMapper.toEventShortDtoSet((entity.getEvents())))
                .build();
    }

    public List<CompilationDto> toDtoList(List<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }
}