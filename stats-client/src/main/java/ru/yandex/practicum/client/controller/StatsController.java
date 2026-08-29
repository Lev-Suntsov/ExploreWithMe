package ru.yandex.practicum.client.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.EndpointHitDto;
import ru.yandex.practicum.ViewStats;
import ru.yandex.practicum.client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/stats")
@Validated
@AllArgsConstructor
public class StatsController {

    private final StatsClient client;

    @GetMapping
    public List<ViewStats> findAllStats(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull LocalDateTime start,

            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull LocalDateTime end,

            @RequestParam(required = false)
            List<String> uris,

            @RequestParam(defaultValue = "false")
            Boolean unique) {

        if (unique) {
            return client.findUniqueStats(start, end, uris);
        }

        return client.findAllStats(start, end, uris);
    }

    @GetMapping("/ua")
    public List<ViewStats> findUniqueStats(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull LocalDateTime start,

            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull LocalDateTime end,

            @RequestParam(required = false)
            List<String> uris) {

        return client.findUniqueStats(start, end, uris);
    }

    private void saveHit(HttpServletRequest request) {
        EndpointHitDto hit = new EndpointHitDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );

        client.saveHit(hit);
    }
}
