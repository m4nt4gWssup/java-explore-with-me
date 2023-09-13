package ru.practicum.adminService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.baseService.enums.State;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestParamForEvent {
    @NotEmpty
    private List<@Positive Long> users;
    private List<@NotNull State> states;
    @NotEmpty
    private List<@Positive Long> categories;
    @FutureOrPresent
    private LocalDateTime rangeStart;
    @Future
    private LocalDateTime rangeEnd;
    @PositiveOrZero
    private int from;
    @Positive
    private int size;
}