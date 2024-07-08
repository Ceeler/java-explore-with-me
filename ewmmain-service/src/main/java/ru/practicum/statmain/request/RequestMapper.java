package ru.practicum.statmain.request;

import ru.practicum.statmain.event.Event;
import ru.practicum.statmain.request.dto.RequestResponse;
import ru.practicum.statmain.request.enums.Status;
import ru.practicum.statmain.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {

    private RequestMapper() {

    }

    public static RequestResponse toDto(Request request) {
        return RequestResponse.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }

    public static List<RequestResponse> toDto(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Request toEntity(User user, Event event) {
        Request request = Request.builder()
                .event(event)
                .requester(user)
                .status(Status.PENDING)
                .build();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
        }

        return request;
    }
}
