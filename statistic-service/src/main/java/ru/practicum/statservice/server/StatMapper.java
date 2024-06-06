package ru.practicum.statservice.server;

import ru.practicum.statservice.dto.EndpointHit;
import ru.practicum.statservice.dto.EndpointStat;

import java.util.List;
import java.util.stream.Collectors;

public class StatMapper {

    public static EndpointStat toDto(StatCountHits statEntity)  {
        return EndpointStat.builder()
                .app(statEntity.getApp())
                .uri(statEntity.getUri())
                .hits(statEntity.getHits())
                .build();
    }

    public static StatEntity toEntity(EndpointHit dto) {
        return StatEntity.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public static List<EndpointStat> toDtoList(List<StatCountHits> statEntities)   {
        return statEntities.stream()
                .map(StatMapper::toDto)
                .collect(Collectors.toList());
    }

}
