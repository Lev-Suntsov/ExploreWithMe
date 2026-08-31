package ru.yandex.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.dto.EventDto;
import ru.yandex.practicum.model.dto.UpdateEventAdminRequest;
import ru.yandex.practicum.model.state.EventState;
import ru.yandex.practicum.service.impl.EventServiceImpl;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final EventServiceImpl eventService;

    @GetMapping
    public List<EventDto> findEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeStart,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeEnd,

            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return eventService.findAdminEvents(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size
        );
    }

    @PatchMapping("/{eventId}")
    public EventDto updateEventByAdmin(
            @PathVariable Long eventId,
            @RequestBody UpdateEventAdminRequest dto
    ) throws BadRequestException {
        return eventService.updateEventByAdmin(eventId, dto);
    }
}