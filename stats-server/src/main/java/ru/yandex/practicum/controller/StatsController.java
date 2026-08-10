package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.ViewStats;
import ru.yandex.practicum.service.StatsServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/stats")
@AllArgsConstructor
public class StatsController {
    StatsServiceImpl service;

    @GetMapping
    public List<ViewStats> findAllStats(@RequestParam LocalDateTime start,
                                        @RequestParam LocalDateTime end,
                                        @RequestParam List<String> uris) {
        return service.findAllStats(start, end, uris);
    }

    @GetMapping("/ua")
    public List<ViewStats> findUniqueStats(@RequestParam LocalDateTime start,
                                           @RequestParam LocalDateTime end,
                                           @RequestParam List<String> uris) {
        return service.findUniqueStats(start, end, uris);
    }
}