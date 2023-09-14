package ru.practicum.baseService.dto.comment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateCommentDto {
    private Long id;
    private String text;
    private String authorName;
    private Long eventId;
    private LocalDateTime created;
    private LocalDateTime edited;
}
