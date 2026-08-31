package ru.yandex.practicum.controllers;

import lombok.RequiredArgsConstructor;
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
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class PublicEventController {

    private final EventServiceImpl eventService;
    private final StatsClient statsClient;

    @GetMapping
    public List<EventShortDto> findEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeStart,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeEnd,

            @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,

            HttpServletRequest request
    ) {
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

    private void saveHit(HttpServletRequest request) {
        EndpointHitDto hit = new EndpointHitDto();

        hit.setApp("ewm-main-service");
        hit.setUri(request.getRequestURI());
        hit.setIp(request.getRemoteAddr());
        hit.setTimestamp(LocalDateTime.now());

        statsClient.saveHit(hit);
    }
}

