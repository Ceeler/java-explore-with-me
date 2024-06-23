package ru.practicum.statmain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statmain.event.EventService;
import ru.practicum.statmain.event.dto.EventFullDto;
import ru.practicum.statmain.event.dto.EventPatchDto;
import ru.practicum.statmain.event.dto.EventPostDto;
import ru.practicum.statmain.event.dto.EventShortDto;
import ru.practicum.statmain.request.RequestService;
import ru.practicum.statmain.request.dto.RequestDto;

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
    public EventFullDto addEvent(@PathVariable Long id, @RequestBody @Valid EventPostDto dto) {
        log.info("Получен запрос POST /users/{}/events body={}", id, dto);
        EventFullDto response = eventService.addEvent(dto, id);
        log.info("Ответ отправлен на запрос POST /users/{}/events body={}", id, response);
        return response;
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /users/{}/events?from={}&size={}", userId, from, size);
        List<EventShortDto> response = eventService.getUserEvents(userId, from, size);
        log.info("Ответ отправлен на запрос GET  /users/{}/events?from={}&size={} body={}", userId, from, size, response.size());
        return response;
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}", userId, eventId);
        EventFullDto response = eventService.getUserEvent(userId, eventId);
        log.info("Ответ отправлен на запрос GET /users/{}/events/{} body={}", userId, eventId, response);
        return response;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                    @RequestBody @Valid EventPatchDto dto) {
        log.info("Получен запрос PATCH /users/{}/events/{} body={}", userId, eventId, dto);
        EventFullDto response = eventService.patchUserEvent(dto, userId, eventId);
        log.info("Ответ отправлен на запрос PATCH /users/{}/events/{} body={}", userId, eventId, response);
        return response;
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getUserEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}/requests", userId, eventId);
        List<RequestDto> response = requestService.getUserEventRequests(userId, eventId);
        log.info("Ответ отправлен на запрос GET /users/{}/events/{}/requests body={}", userId, eventId, response.size());
        return response;
    }

}
