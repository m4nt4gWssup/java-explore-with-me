package ru.practicum.ewm.baseService.dto.location;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
}