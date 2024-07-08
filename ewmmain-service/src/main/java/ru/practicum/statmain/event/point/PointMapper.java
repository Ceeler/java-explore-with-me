package ru.practicum.statmain.event.point;

import ru.practicum.statmain.event.Event;

public class PointMapper {

    private PointMapper() {

    }

    public static Event.Point toEntity(PointResponseRequest dto) {
        return new Event.Point(dto.getLat(), dto.getLon());
    }

    public static PointResponseRequest toDto(Event.Point dto) {
        return new PointResponseRequest(dto.getLat(), dto.getLon());
    }
}
