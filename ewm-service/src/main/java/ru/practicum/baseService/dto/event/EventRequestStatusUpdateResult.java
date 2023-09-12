package ru.practicum.baseService.dto.event;

import lombok.*;
import ru.practicum.baseService.dto.request.ParticipationRequestDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}