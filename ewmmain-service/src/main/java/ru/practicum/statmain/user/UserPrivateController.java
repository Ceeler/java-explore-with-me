package ru.practicum.statmain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statmain.event.EventService;
import ru.practicum.statmain.event.dto.EventFullResponse;
import ru.practicum.statmain.event.dto.EventPostRequest;
import ru.practicum.statmain.event.dto.EventShortResponse;
import ru.practicum.statmain.event.dto.EventUserPatchRequest;
import ru.practicum.statmain.request.RequestService;
import ru.practicum.statmain.request.dto.RequestPatchRequest;
import ru.practicum.statmain.request.dto.RequestPatchResponse;
import ru.practicum.statmain.request.dto.RequestResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserPrivateController {

    private final EventService eventService;

    private final RequestService requestService;

    @PostMapping("/{id}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullResponse addEvent(@PathVariable Long id, @RequestBody @Valid EventPostRequest dto) {
        log.info("Получен запрос POST /users/{}/events body={}", id, dto);
        EventFullResponse response = eventService.addEvent(dto, id);
        log.info("Ответ отправлен на запрос POST /users/{}/events body={}", id, response);
        return response;
    }

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortResponse> getUserEvents(@PathVariable Long userId,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /users/{}/events?from={}&size={}", userId, from, size);
        List<EventShortResponse> response = eventService.getUserEvents(userId, from, size);
        log.info("Ответ отправлен на запрос GET  /users/{}/events?from={}&size={} body={}", userId, from, size, response.size());
        return response;
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullResponse getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}", userId, eventId);
        EventFullResponse response = eventService.getUserEvent(userId, eventId);
        log.info("Ответ отправлен на запрос GET /users/{}/events/{} body={}", userId, eventId, response);
        return response;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullResponse updateEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                         @RequestBody @Valid EventUserPatchRequest dto) {
        log.info("Получен запрос PATCH /users/{}/events/{} body={}", userId, eventId, dto);
        EventFullResponse response = eventService.patchUserEvent(dto, userId, eventId);
        log.info("Ответ отправлен на запрос PATCH /users/{}/events/{} body={}", userId, eventId, response);
        return response;
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestResponse> getUserEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}/requests", userId, eventId);
        List<RequestResponse> response = requestService.getUserEventRequests(userId, eventId);
        log.info("Ответ отправлен на запрос GET /users/{}/events/{}/requests body={}", userId, eventId, response.size());
        return response;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestPatchResponse patchUserEventRequests(@PathVariable Long userId, @PathVariable Long eventId,
                                                       @Valid @RequestBody RequestPatchRequest dto) {
        log.info("Получен запрос PATCH /users/{}/events/{}/requests body={}", userId, eventId, dto);
        RequestPatchResponse response = requestService.patchUserEventRequests(dto, userId, eventId);
        log.info("Ответ отправлен на запрос PATCH /users/{}/events/{}/requests body={}", userId, eventId, response);
        return response;
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestResponse> getUserRequests(@PathVariable Long userId) {
        log.info("Получен запрос GET /users/{}/requests", userId);
        List<RequestResponse> response = requestService.getRequests(userId);
        log.info("Ответ отправлен на запрос GET /users/{}/requests body={}", userId, response.size());
        return response;
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponse addRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Получен запрос POST /users/{}/requests?eventId={}", userId, eventId);
        RequestResponse response = requestService.createRequest(userId, eventId);
        log.info("Ответ отправлен на запрос POST /users/{}/requests?eventId={} body={}", userId, eventId, response);
        return response;
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestResponse cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Получен запрос PATCH /users/{}/requests/{}/cancel", userId, requestId);
        RequestResponse response = requestService.cancelRequest(userId, requestId);
        log.info("Ответ отправлен на запрос PATCH /users/{}/requests/{}/cancel body={}", userId, requestId, response);
        return response;
    }

}
