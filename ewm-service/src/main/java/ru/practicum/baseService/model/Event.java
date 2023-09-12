package ru.practicum.baseService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.baseService.enums.State;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "annotation")
    @NotNull
    private String annotation;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;
    @Column(name = "confirmedRequests")
    private long confirmedRequests;
    @Column(name = "createdOn")
    private LocalDateTime createdOn;
    @Column(name = "description")
    @NotNull
    private String description;
    @Column(name = "date")
    private LocalDateTime date;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    @NotNull
    private User initiator;
    @AttributeOverrides(value = {
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    private Location location;
    @Column(name = "paid")
    @NotNull
    private Boolean paid;
    @Column(name = "participantLimit")
    private Long participantLimit;
    @Column(name = "publishedOn")
    private LocalDateTime publishedOn;
    @Column(name = "requestModeration")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;
    @Column(name = "title")
    @NotBlank
    private String title;
    @Column(name = "views")
    private Long views;
}