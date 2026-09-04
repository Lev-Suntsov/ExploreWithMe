package ru.yandex.practicum.controllers;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.apache.coyote.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.EndpointHitDto;
import ru.yandex.practicum.client.StatsClient;
import ru.yandex.practicum.model.dto.EventDto;
import ru.yandex.practicum.model.dto.EventShortDto;
import ru.yandex.practicum.service.impl.EventServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventServiceImpl eventService;
    private final StatsClient statsClient;

    @GetMapping
    public List<EventShortDto> findEvents(
            @PathVariable(required = false) String text,
            @PathVariable(required = false) List<Long> categories,
            @PathVariable(required = false) Boolean paid,

            @PathVariable(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeStart,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeEnd,

            @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "10") String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,

            HttpServletRequest request
    ) throws BadRequestException {
        saveHit(request);

        return eventService.findPublicEvents(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size
        );
    }

    @GetMapping("/{id}")
    public EventDto findEvent(@PathVariable Long id, HttpServletRequest request) {
        saveHit(request);

        return eventService.findPublicEvent(id);
    }

    private void saveHit(@Valid HttpServletRequest request) {
        EndpointHitDto hit = new EndpointHitDto();

        hit.setApp("ewm-main-service");
        hit.setUri(request.getRequestURI());
        hit.setIp(request.getRemoteAddr());
        hit.setTimestamp(LocalDateTime.now());

        statsClient.saveHit(hit);
    }
}

