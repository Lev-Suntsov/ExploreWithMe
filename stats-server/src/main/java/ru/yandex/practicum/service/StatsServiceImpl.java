package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.EndpointHitDto;
import ru.yandex.practicum.ViewStats;
import ru.yandex.practicum.exeptions.ConflictException;
import ru.yandex.practicum.mapper.StatsMapper;
import ru.yandex.practicum.model.StatsRow;
import ru.yandex.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private StatsRepository repository;

    @Transactional
    public EndpointHitDto saveHit(EndpointHitDto endpointHitDto) {
        return StatsMapper.endpointHitToDto(repository.save(StatsMapper.endpointHitFromDto(endpointHitDto)));
    }

    @Override
    public List<ViewStats> findAllStats(LocalDateTime start,
                                        LocalDateTime end,
                                        List<String> uris) {
        List<StatsRow> rows;

        if (start.isAfter(end)) {
            throw new ConflictException("время начала должно быть раньше конца");
        }

        if (uris == null || uris.isEmpty()) {
            rows = repository.findAllStatsWithoutUris(start, end);
        } else {
            rows = repository.findAllStats(start, end, uris);
        }

        return rows.stream()
                .map(StatsMapper::toViewStats)
                .collect(Collectors.toList());
    }

    @Override
    public List<ViewStats> findUniqueStats(LocalDateTime start,
                                           LocalDateTime end,
                                           List<String> uris) {
        List<StatsRow> rows;

        if (start.isAfter(end)) {
            throw new ConflictException("время начала должно быть раньше конца");
        }

        if (uris == null || uris.isEmpty()) {
            rows = repository.findUniqueStatsWithoutUris(start, end);
        } else {
            rows = repository.findUniqueStats(start, end, uris);
        }

        return rows.stream()
                .map(StatsMapper::toViewStats)
                .collect(Collectors.toList());
    }


}
