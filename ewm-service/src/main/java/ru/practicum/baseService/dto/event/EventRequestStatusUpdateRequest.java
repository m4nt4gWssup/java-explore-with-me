package ru.practicum.baseService.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestStatusUpdateRequest {
    @NotNull
    private List<Long> requestIds;

    @NotBlank
    private String status;

    @Override
    public String toString() {
        return "EventRequestStatusUpdateRequest{" +
                "requestIds=" + requestIds.toString() +
                ", status='" + status + '\'' +
                '}';
    }
}