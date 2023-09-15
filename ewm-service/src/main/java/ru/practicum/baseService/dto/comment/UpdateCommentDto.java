package ru.practicum.baseService.dto.comment;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class UpdateCommentDto {
    private Long id;
    @NotBlank
    private String text;
    @NotBlank
    private String authorName;
    @NotNull
    private Long eventId;
    @NotNull
    private LocalDateTime created;
    @NotNull
    private LocalDateTime edited;
}
