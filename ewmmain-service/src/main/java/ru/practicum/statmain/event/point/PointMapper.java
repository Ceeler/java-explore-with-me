package ru.practicum.statmain.event.point;

import ru.practicum.statmain.event.Event;

public class PointMapper {

    private PointMapper() {

    }

    ;

    public static Event.Point toEntity(PointDto dto) {
        return new Event.Point(dto.getLat(), dto.getLon());
    }

    public static PointDto toDto(Event.Point dto) {
        return new PointDto(dto.getLat(), dto.getLon());
    }
}
