package ru.practicum.statmain.event;

import ru.practicum.statmain.category.Category;
import ru.practicum.statmain.category.CategoryMapper;
import ru.practicum.statmain.event.dto.*;
import ru.practicum.statmain.event.enums.State;
import ru.practicum.statmain.event.point.PointMapper;
import ru.practicum.statmain.user.User;
import ru.practicum.statmain.user.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {

    private EventMapper() {
    }

    public static Event toEntity(EventPostRequest dto, Category category, User initiator) {
        return Event.builder()
                .title(dto.getTitle())
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .paid(dto.getPaid() != null && dto.getPaid())
                .participantLimit(dto.getParticipantLimit() == null ? 0 : dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration() == null || dto.getRequestModeration())
                .eventDate(dto.getEventDate())
                .point(PointMapper.toEntity(dto.getLocation()))
                .state(State.PENDING)
                .confirmedRequests(new ArrayList<>())
                .requests(new ArrayList<>())
                .initiator(initiator)
                .category(category)
                .build();
    }

    public static EventShortResponse toEventShortDto(Event event) {
        return EventShortResponse.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toResponse(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests().size())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static List<EventShortResponse> toEventShortDto(List<Event> events) {
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public static EventFullResponse toEventFullDto(Event event) {
        return EventFullResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .category(CategoryMapper.toResponse(event.getCategory()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .confirmedRequests(event.getConfirmedRequests().size())
                .createdOn(event.getCreatedOn())
                .location(PointMapper.toDto(event.getPoint()))
                .publishedOn(event.getPublishedOn())
                .views(event.getViews())
                .build();
    }

    public static List<EventFullResponse> toEventFullDto(List<Event> event) {
        return event.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    public static void updateEntity(Event event, EventUserPatchRequest dto) {
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setPoint(PointMapper.toEntity(dto.getLocation()));
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
    }

    public static void updateEntity(Event event, EventAdminPatchRequest dto) {
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setPoint(PointMapper.toEntity(dto.getLocation()));
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
    }

}
