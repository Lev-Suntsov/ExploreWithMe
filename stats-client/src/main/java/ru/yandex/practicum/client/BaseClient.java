package ru.yandex.practicum.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

public abstract class BaseClient {

    protected final RestTemplate restTemplate;
    private final String serverUrl;

    protected BaseClient(String serverUrl, RestTemplate restTemplate) {
        this.serverUrl = serverUrl;
        this.restTemplate = restTemplate;
    }

    protected static URI buildUri(String path,
                                  String start,
                                  String end,
                                  List<String> uris) {

        UriComponentsBuilder builder =
                UriComponentsBuilder
                        .fromHttpUrl(serverUrl + path)
                        .queryParam("start", start)
                        .queryParam("end", end);

        if (uris != null) {
            for (String uri : uris) {
                builder.queryParam("uris", uri);
            }
        }

        return builder.encode().build().toUri();
    }

    protected static <T> ResponseEntity<T> get(
            URI uri,
            ParameterizedTypeReference<T> responseType) {

        return restTemplate.exchange(
                uri,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                responseType
        );
    }
}
