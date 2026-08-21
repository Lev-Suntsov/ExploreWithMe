package ru.yandex.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class StatsClient extends BaseClient {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClient(@Value("${STATS_SERVER_URL:http://stats-server:9090}") String serverUrl, RestTemplate restTemplate) {
        super(serverUrl, restTemplate);
    }

    public List<ViewStats> findAllStats(LocalDateTime start,
                                        LocalDateTime end,
                                        List<String> uris) {

        String startString = start.format(FORMATTER);
        String endString = end.format(FORMATTER);

        return getStats("/stats", startString, endString, uris);
    }

    public List<ViewStats> findUniqueStats(LocalDateTime start,
                                           LocalDateTime end,
                                           List<String> uris) {

        String startString = start.format(FORMATTER);
        String endString = end.format(FORMATTER);

        return getStats("/stats/ua", startString, endString, uris);
    }

    private List<ViewStats> getStats(String path,
                                     String start,
                                     String end,
                                     List<String> uris) {

        var uri = buildUri(path, start, end, uris);

        var response = get(
                uri,
                new ParameterizedTypeReference<List<ViewStats>>() {
                }
        );

        return response.getBody() == null
                ? Collections.emptyList()
                : response.getBody();
    }
}