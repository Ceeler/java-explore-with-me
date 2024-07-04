package ru.practicum.statmain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statmain.event.EventService;
import ru.practicum.statmain.event.dto.EventFullDto;
import ru.practicum.statmain.event.dto.EventPostDto;
import ru.practicum.statmain.event.dto.EventShortDto;
import ru.practicum.statmain.event.dto.EventUserPatchDto;
import ru.practicum.statmain.request.RequestService;
import ru.practicum.statmain.request.dto.RequestDto;
import ru.practicum.statmain.request.dto.RequestPatchDto;
import ru.practicum.statmain.request.dto.RequestPatchResultDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final EventService eventService;

    private final RequestService requestService;

    @PostMapping("/{id}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long id, @RequestBody @Valid EventPostDto dto) {
        log.info("Получен запрос POST /users/{}/events body={}", id, dto);
        EventFullDto response = eventService.addEvent(dto, id);
        log.info("Ответ отправлен на запрос POST /users/{}/events body={}", id, response);
        return response;
    }

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /users/{}/events?from={}&size={}", userId, from, size);
        List<EventShortDto> response = eventService.getUserEvents(userId, from, size);
        log.info("Ответ отправлен на запрос GET  /users/{}/events?from={}&size={} body={}", userId, from, size, response.size());
        return response;
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}", userId, eventId);
        EventFullDto response = eventService.getUserEvent(userId, eventId);
        log.info("Ответ отправлен на запрос GET /users/{}/events/{} body={}", userId, eventId, response);
        return response;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                    @RequestBody @Valid EventUserPatchDto dto) {
        log.info("Получен запрос PATCH /users/{}/events/{} body={}", userId, eventId, dto);
        EventFullDto response = eventService.patchUserEvent(dto, userId, eventId);
        log.info("Ответ отправлен на запрос PATCH /users/{}/events/{} body={}", userId, eventId, response);
        return response;
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getUserEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}/requests", userId, eventId);
        List<RequestDto> response = requestService.getUserEventRequests(userId, eventId);
        log.info("Ответ отправлен на запрос GET /users/{}/events/{}/requests body={}", userId, eventId, response.size());
        return response;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestPatchResultDto patchUserEventRequests(@PathVariable Long userId, @PathVariable Long eventId,
                                                        @Valid @RequestBody RequestPatchDto dto) {
        log.info("Получен запрос PATCH /users/{}/events/{}/requests body={}", userId, eventId, dto);
        RequestPatchResultDto response = requestService.patchUserEventRequests(dto, userId, eventId);
        log.info("Ответ отправлен на запрос PATCH /users/{}/events/{}/requests body={}", userId, eventId, response);
        return response;
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getUserRequests(@PathVariable Long userId) {
        log.info("Получен запрос GET /users/{}/requests", userId);
        List<RequestDto> response = requestService.getRequests(userId);
        log.info("Ответ отправлен на запрос GET /users/{}/requests body={}", userId, response.size());
        return response;
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Получен запрос POST /users/{}/requests?eventId={}", userId, eventId);
        RequestDto response = requestService.createRequest(userId, eventId);
        log.info("Ответ отправлен на запрос POST /users/{}/requests?eventId={} body={}", userId, eventId, response);
        return response;
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Получен запрос PATCH /users/{}/requests/{}/cancel", userId, requestId);
        RequestDto response = requestService.cancelRequest(userId, requestId);
        log.info("Ответ отправлен на запрос PATCH /users/{}/requests/{}/cancel body={}", userId, requestId, response);
        return response;
    }

}
