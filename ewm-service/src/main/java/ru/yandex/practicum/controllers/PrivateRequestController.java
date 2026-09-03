package ru.yandex.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.EventRequestStatusUpdateResult;
import ru.yandex.practicum.model.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.model.dto.ParticipationRequestDto;
import ru.yandex.practicum.service.RequestService; // standard interface injection

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class PrivateRequestController {

    private final RequestService requestService; // Inject Interface

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        return requestService.getUserRequests(userId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) throws BadRequestException { // Removed Tomcat BadRequestException import dependency
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) {
        // Enforce argument ordering: user id first, request id second
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipants(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return requestService.getEventParticipants(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestStatus(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest dto // Kept proper RequestBody mapping placement
    ) throws BadRequestException {
        return requestService.changeRequestStatus(userId, eventId, dto);
    }
}
