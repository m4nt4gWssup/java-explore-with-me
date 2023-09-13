package ru.practicum.baseService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Float lat;
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Float lon;
}