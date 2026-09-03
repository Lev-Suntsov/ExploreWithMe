package ru.yandex.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.EndpointHitDto;
import ru.yandex.practicum.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class StatsClient extends BaseClient {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClient(@Value("${STATS_SERVER_URL:http://stats-server:9090}") String serverUrl, RestTemplateBuilder builder) {
        super(serverUrl, builder.build());
    }

    public List<ViewStats> findAllStats(LocalDateTime start,
                                        LocalDateTime end,
                                        List<String> uris) {

        String startString = start.format(FORMATTER);
        List<String> endString = Collections.singletonList(end.format(FORMATTER));

        return getStats("/stats", startString, endString, uris);
    }

    public List<ViewStats> findUniqueStats(LocalDateTime start,
                                           LocalDateTime end,
                                           List<String> uris) {

        String startString = start.format(FORMATTER);
        String endString = end.format(FORMATTER);

        return getStats("/stats/ua", startString, Collections.singletonList(endString), uris);
    }

    // Inside ru.yandex.practicum.client.StatsClient.java

    // ---> FIX 1: Change visibility to PUBLIC <---
    public List<ViewStats> getStats(String start, String end, List<String> uris, List<String> unique) {

        // Pass the standard stats endpoint path as the first argument to your internal router engine
        // If your buildUri method accepts a unique boolean flag, you can pass it to buildUri as well
        var uri = buildUri("/stats", start, end, uris);

        // Adjusting type references to use your localized ViewStats class structure context
        var response = get(
                uri,
                new ParameterizedTypeReference<List<ViewStats>>() {
                }
        );

        return response.getBody() == null
                ? Collections.emptyList()
                : response.getBody();
    }


    @Value("${stats-server.url:http://stats-server:9090}")
    private String statsServerUrl;

    public void saveHit(EndpointHitDto hit) {
        restTemplate.postForEntity(
                statsServerUrl + "/hit",
                hit,
                Void.class
        );
    }
}