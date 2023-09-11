package ru.practicum.ewm.baseService.dto.event;

import lombok.*;
import ru.practicum.ewm.baseService.dto.request.ParticipationRequestDto;

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