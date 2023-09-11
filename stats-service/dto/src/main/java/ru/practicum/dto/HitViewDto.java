package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HitViewDto {
    private String app;
    private String uri;
    private Long hits;
}
