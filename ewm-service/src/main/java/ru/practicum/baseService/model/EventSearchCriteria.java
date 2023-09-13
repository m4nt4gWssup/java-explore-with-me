package ru.practicum.baseService.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventSearchCriteria {
    @NotBlank
    private String text;
    private List<Long> categories;
    private Boolean paid;
    @FutureOrPresent
    private LocalDateTime rangeStart;
    @Future
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
}