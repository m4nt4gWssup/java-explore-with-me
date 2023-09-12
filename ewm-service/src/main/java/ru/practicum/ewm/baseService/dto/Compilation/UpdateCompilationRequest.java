package ru.practicum.ewm.baseService.dto.Compilation;

import lombok.*;
import ru.practicum.ewm.baseService.util.notblanknull.NotBlankNull;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompilationRequest {
    private Set<Long> events;
    private Boolean pinned;
    @NotBlankNull
    @Size(max = 50)
    private String title;
}