package ru.yandex.practicum.client.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.ViewStats;
import ru.yandex.practicum.client.StatsClient;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/stats")
@Validated
@AllArgsConstructor
public class StatsController {
    StatsClient client;

    @GetMapping
    public List<ViewStats> findAllStats(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull(message = "дата начала должна быть указанна") LocalDateTime start,

            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull(message = "дата конца должна быть указанна") LocalDateTime end,

            @RequestParam(required = false)
            List<String> uris) {

        return client.findAllStats(start, end, uris);
    }

    @GetMapping("/ua")
    public List<ViewStats> findUniqueStats(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull(message = "дата начала должна быть указанна") LocalDateTime start,

            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull(message = "дата конца должна быть указанна") LocalDateTime end,

            @RequestParam(required = false)
            List<String> uris) {

        return client.findUniqueStats(start, end, uris);
    }
}
