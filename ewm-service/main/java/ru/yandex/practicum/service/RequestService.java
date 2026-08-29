package ru.yandex.practicum.service;

import org.apache.coyote.BadRequestException;
import ru.yandex.practicum.model.EventRequestStatusUpdateResult;
import ru.yandex.practicum.model.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeRequestStatus(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest dto
    ) throws BadRequestException;
}
