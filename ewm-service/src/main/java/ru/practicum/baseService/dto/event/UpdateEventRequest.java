package ru.practicum.baseService.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.baseService.dto.location.LocationDto;
import ru.practicum.baseService.util.notblanknull.NotBlankNull;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

import static ru.practicum.baseService.util.constants.Constants.DATE_FORMAT;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class UpdateEventRequest {
    @Length(min = 20, max = 2000)
    @NotBlankNull
    private String annotation;
    private Long category;
    @Length(min = 20, max = 7000)
    @NotBlankNull
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;
    @Valid
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Long participantLimit;
    private Boolean requestModeration;
    @Length(min = 3, max = 120)
    @NotBlankNull
    private String title;

    @Override
    public String toString() {
        return "UpdateEventUserRequest{" +
                "annotation='" + annotation + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", requestModeration=" + requestModeration +
                ", title='" + title + '\'' +
                '}';
    }
}