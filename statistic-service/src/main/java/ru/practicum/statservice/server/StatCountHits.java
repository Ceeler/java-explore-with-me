package ru.practicum.statservice.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatCountHits {

    private String app;

    private String uri;

    private Integer hits;
}
