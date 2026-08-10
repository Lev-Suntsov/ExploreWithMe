package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ViewStats;
import ru.yandex.practicum.mapper.StatsMapper;
import ru.yandex.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    StatsRepository repository;

    @Override
    public List<ViewStats> findAllStats(LocalDateTime start,
                                        LocalDateTime end,
                                        List<String> uris) {
        return repository.findAllStats(start, end, uris).stream().map(StatsMapper::toViewStats).collect(Collectors.toList());
    }

    @Override
    public List<ViewStats> findUniqueStats(LocalDateTime start,
                                           LocalDateTime end,
                                           List<String> uris) {
        return repository.findUniqueStats(start, end, uris).stream().map(StatsMapper::toViewStats).collect(Collectors.toList());
    }
}
