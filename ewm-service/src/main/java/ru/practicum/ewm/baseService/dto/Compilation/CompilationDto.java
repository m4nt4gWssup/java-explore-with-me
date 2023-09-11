package ru.practicum.ewm.baseService.dto.Compilation;

import lombok.*;
import ru.practicum.ewm.baseService.dto.event.EventShortDto;

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