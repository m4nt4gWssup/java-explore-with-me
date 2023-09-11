package ru.practicum.ewm.baseService.dto.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private StateAction stateAction;

    public enum StateAction {
        PUBLISH_EVENT, REJECT_EVENT
    }
}