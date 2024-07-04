package ru.practicum.statmain.request;

import ru.practicum.statmain.event.Event;
import ru.practicum.statmain.request.dto.RequestDto;
import ru.practicum.statmain.request.enums.Status;
import ru.practicum.statmain.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {

    private RequestMapper() {

    }

    public static RequestDto toDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }

    public static List<RequestDto> toDto(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Request toEntity(User user, Event event) {
        return Request.builder()
                .event(event)
                .requester(user)
                .status(Status.PENDING)
                .build();
    }
}
