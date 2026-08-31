package ru.yandex.practicum.model.dto;

import lombok.Data;
import ru.yandex.practicum.model.RequestStatus;

import java.util.List;


@Data
public class   EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestStatus status;
}
