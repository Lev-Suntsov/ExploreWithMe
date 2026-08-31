package ru.yandex.practicum.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.model.dto.ParticipationRequestDto;

import java.util.List;
@NoArgsConstructor
@Data
public class EventRequestStatusUpdateResult {

    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;

    public EventRequestStatusUpdateResult(List confirmedList, List rejectedList) {
        this.confirmedRequests = confirmedList;
        this.rejectedRequests = rejectedList;
    }
}
