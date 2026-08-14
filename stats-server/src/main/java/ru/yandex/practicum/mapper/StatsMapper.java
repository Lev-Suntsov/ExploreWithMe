package ru.yandex.practicum.mapper;

import ru.yandex.practicum.EndpointHitDto;
import ru.yandex.practicum.ViewStats;
import ru.yandex.practicum.model.EndpointHit;
import ru.yandex.practicum.model.StatsRow;

public class StatsMapper {
    public EndpointHit endpointHitFromDto(EndpointHitDto dto) {
        return new EndpointHit(dto.getApp(), dto.getUri(),
                dto.getIp(), dto.getTimestamp());
    }

    public EndpointHitDto endpointHitToDto(EndpointHit endpointHit) {
       return new EndpointHitDto(endpointHit.getApp(), endpointHit.getUri(), endpointHit.getIp(),
               endpointHit.getTimestamp());
    }

    public static ViewStats toViewStats(StatsRow row) {
        ViewStats viewStats = new ViewStats();

        viewStats.setApp(row.getApp());
        viewStats.setUri(row.getUri());
        viewStats.setHits(row.getHits());

        return viewStats;
    }
}
