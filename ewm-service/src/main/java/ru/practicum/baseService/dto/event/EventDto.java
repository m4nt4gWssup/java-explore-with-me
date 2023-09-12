package ru.practicum.baseService.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.baseService.enums.State;
import ru.practicum.baseService.model.Category;
import ru.practicum.baseService.model.Location;
import ru.practicum.baseService.model.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long id;
    private String annotation;
    private Category category;
    private Long confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private User initiator;
    private Location location;
    private boolean paid;
    private Long participantLimit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private State state;
    private String title;
    private Long views;
}
