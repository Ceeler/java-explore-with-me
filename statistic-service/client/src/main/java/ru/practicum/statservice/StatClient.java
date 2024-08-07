package ru.practicum.statservice;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


public class StatClient {

    private final RestTemplate rest;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public StatClient(String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public EndpointHit addHit(String app, String uri, String ip) {
        HttpEntity<EndpointHit> requestEntity = new HttpEntity<>(EndpointHit.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build());

        try {
            ResponseEntity<EndpointHit> response = rest.exchange("/hit", HttpMethod.POST, requestEntity, EndpointHit.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Stat service is unavailable");
        }
    }

    public List<EndpointStat> getAll(LocalDateTime start, LocalDateTime end, Boolean unique, String... uris) {
        try {
            String startParam = URLEncoder.encode(start.format(formatter), "UTF-8");
            String endParam  = URLEncoder.encode(end.format(formatter),  "UTF-8");
            ResponseEntity<EndpointStat[]> response = rest.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}", HttpMethod.GET, null, EndpointStat[].class,
            startParam, endParam, uris, unique);
            return Arrays.asList(response.getBody());
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Stat service is unavailable");
        } catch (UnsupportedEncodingException e)  {
            throw new IllegalArgumentException("Unsupported encoding");
        }
    }

}
