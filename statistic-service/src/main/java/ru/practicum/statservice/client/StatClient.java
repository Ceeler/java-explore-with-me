package ru.practicum.statservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.statservice.dto.EndpointHit;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.URLEncoder;

@Service
public class StatClient {

    private final RestTemplate rest;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public StatClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> addHit(String app, String uri, String ip) {
        HttpEntity<EndpointHit> requestEntity = new HttpEntity<>(EndpointHit.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build());

        try {
            ResponseEntity<Object> response = rest.exchange("/hit", HttpMethod.POST, requestEntity, Object.class);
            return ResponseEntity.status(response.getStatusCode()).build();
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> getAll(LocalDateTime start, LocalDateTime end, Boolean unique, String... uris)  {
        try {
            String startParam = URLEncoder.encode(start.format(formatter), "UTF-8");
            String endParam  = URLEncoder.encode(end.format(formatter),  "UTF-8");
            ResponseEntity<Object> response = rest.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}", HttpMethod.GET, null, Object.class,
            startParam, endParam, uris, unique);
            return ResponseEntity.status(response.getStatusCode()).build();
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        } catch (UnsupportedEncodingException e)  {
            throw new IllegalArgumentException("Unsupported encoding");
        }
    }

}
