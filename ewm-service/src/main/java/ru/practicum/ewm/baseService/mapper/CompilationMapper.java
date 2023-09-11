package ru.practicum.ewm.baseService.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.baseService.dto.Compilation.CompilationDto;
import ru.practicum.ewm.baseService.dto.Compilation.NewCompilationDto;
import ru.practicum.ewm.baseService.model.Compilation;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class CompilationMapper {

    public Compilation toEntity(NewCompilationDto dto) {
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
                .events(EventMapper.toEventShortDtoList((entity.getEvents())))
                .build();
    }

    public List<CompilationDto> toDtoList(List<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }
}