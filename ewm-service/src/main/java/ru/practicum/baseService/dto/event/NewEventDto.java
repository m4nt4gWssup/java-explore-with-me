package ru.practicum.baseService.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.baseService.dto.location.LocationDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

import static ru.practicum.baseService.util.constants.Constants.DATE_FORMAT;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewEventDto {
    @Length(min = 20, max = 2000)
    @NotBlank
    private String annotation;
    @NotNull
    private Long category;
    @Length(min = 20, max = 7000)
    @NotBlank
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @NotNull
    private LocalDateTime eventDate;
    @NotNull
    @Valid
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private long participantLimit;
    private Boolean requestModeration;
    @Length(min = 3, max = 120)
    @NotBlank
    private String title;

    @Override
    public String toString() {
        return "NewEventDto{" +
                "annotation='" + annotation + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", requestModeration=" + requestModeration +
                ", title='" + title + '\'' +
                '}';
    }
}