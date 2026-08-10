package ru.yandex.practicum.mapper;

import ru.yandex.practicum.EndpointHitDto;
import ru.yandex.practicum.ViewStats;
import ru.yandex.practicum.model.EndpointHit;
import ru.yandex.practicum.model.StatsRow;

public class StatsMapper {
    public EndpointHit endpointHitFromDto(EndpointHitDto dto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setId(dto.getId());
        endpointHit.setIp(dto.getIp());
        endpointHit.setUri(dto.getUri());
        endpointHit.setApp(dto.getApp());
        endpointHit.setTimestamp(dto.getTimestamp());

        return endpointHit;
    }

    public EndpointHitDto endpointHitToDto(EndpointHit endpointHit) {
        EndpointHitDto dto = new EndpointHitDto();
        dto.setApp(endpointHit.getApp());
        dto.setUri(endpointHit.getUri());
        dto.setIp(endpointHit.getIp());
        dto.setId(endpointHit.getId());
        dto.setTimestamp(endpointHit.getTimestamp());

        return dto;
    }

    public static ViewStats toViewStats(StatsRow row) {
        ViewStats viewStats = new ViewStats();

        viewStats.setApp(row.getApp());
        viewStats.setUri(row.getUri());
        viewStats.setHits(row.getHits());

        return viewStats;
    }
}
