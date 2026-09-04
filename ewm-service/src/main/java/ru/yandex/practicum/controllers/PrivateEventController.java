package ru.yandex.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.dto.EventDto;
import ru.yandex.practicum.model.dto.EventShortDto;
import ru.yandex.practicum.model.dto.NewEventDto;
import ru.yandex.practicum.model.dto.UpdateEventUserRequest;
import ru.yandex.practicum.service.impl.EventServiceImpl;

import javax.validation.Valid;
import java.util.List;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/users/events")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {

    private final EventServiceImpl eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto addEvent(
            @NotNull(message = "id не может быть пустым") @RequestParam(name = "X-Sharer-User-Id") Long userId,
            @RequestBody @Valid NewEventDto dto
    ) throws BadRequestException {
        return eventService.createEvent(userId, dto);
    }

    @GetMapping
    public List<EventShortDto> getEvents(
            @NotNull(message = "id не может быть пустым") @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventDto getEvent(
            @PathVariable @NotNull(message = "id не может быть пустым") Long userId,
            @PathVariable @NotNull(message = "id не может быть пустым") Long eventId
    ) {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventDto updateEvent(
            @PathVariable @NotNull(message = "id не может быть пустым") Long userId,
            @PathVariable @NotNull(message = "id не может быть пустым") Long eventId,
            @RequestBody  @Valid UpdateEventUserRequest dto
    ) throws BadRequestException {
        return eventService.updateEvent(userId, eventId, dto);
    }
}
