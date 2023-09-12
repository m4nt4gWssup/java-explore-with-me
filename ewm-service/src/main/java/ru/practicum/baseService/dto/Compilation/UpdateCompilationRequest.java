package ru.practicum.baseService.dto.Compilation;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.baseService.util.notblanknull.NotBlankNull;

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
    @Length(min = 2, max = 50)
    private String title;
}