package ru.practicum.baseService.dto.Compilation;

import lombok.*;
import ru.practicum.baseService.dto.event.EventShortDto;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDto {
    private Set<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}