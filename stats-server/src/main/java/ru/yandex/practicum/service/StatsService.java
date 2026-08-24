package ru.yandex.practicum.service;

import ru.yandex.practicum.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<ViewStats> findAllStats(LocalDateTime start,
                                 LocalDateTime end,
                                 List<String> uris);

    List<ViewStats> findUniqueStats(LocalDateTime start,
                                   LocalDateTime end,
                                   List<String> uris);


}
