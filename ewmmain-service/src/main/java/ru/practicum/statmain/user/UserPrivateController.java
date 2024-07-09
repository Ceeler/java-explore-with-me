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
import ru.practicum.statmain.user.dto.UserShortResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserPrivateController {

    private final EventService eventService;

    private final RequestService requestService;

    private final UserService userService;

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

    @GetMapping("/{userId}/subscriptions")
    @ResponseStatus(HttpStatus.OK)
    public List<UserShortResponse> getUserSubscriptions(@PathVariable Long userId) {
        log.info("Получен запрос GET /users/{}/subscriptions", userId);
        List<UserShortResponse> response = userService.getUserSubscriptions(userId);
        log.info("Ответ отправлен на запрос GET /users/{}/subscriptions body={}", userId, response.size());
        return response;
    }

    @PostMapping("/{userId}/subscribe/{subscribedOnId}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UserShortResponse> subscribe(@PathVariable Long userId, @PathVariable Long subscribedOnId) {
        log.info("Получен запрос POST /users/{}/subscribe/{}", userId, subscribedOnId);
        List<UserShortResponse> response = userService.subscribe(userId, subscribedOnId);
        log.info("Ответ отправлен на запрос POST /users/{}/subscribe/{}", userId, subscribedOnId);
        return response;
    }

    @GetMapping("/{userId}/subscribers")
    @ResponseStatus(HttpStatus.OK)
    public List<UserShortResponse> getUserSubscribers(@PathVariable Long userId) {
        log.info("Получен запрос GET /users/{}/subscribers", userId);
        List<UserShortResponse> response = userService.getUserSubscribers(userId);
        log.info("Ответ отправлен на запрос GET /users/{}/subscribers body={}", userId, response.size());
        return response;
    }

    @GetMapping("/{userId}/subscriptions/{subscribedOnId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortResponse> getSubscriptionEvent(@PathVariable Long userId, @PathVariable Long subscribedOnId) {
        log.info("Получен запрос GET /users/{}/subscriptions/{}", userId, subscribedOnId);
        List<EventShortResponse> response = userService.getSubscriberEvent(userId, subscribedOnId);
        log.info("Ответ отправлен на запрос GET /users/{}/subscriptions/{} body={}", userId, subscribedOnId, response.size());
        return response;
    }

    @DeleteMapping("/{userId}/subscribers/{subscriberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserSubscriber(@PathVariable Long userId, @PathVariable Long subscriberId) {
        log.info("Получен запрос DELETE /users/{}/subscriptions/{}", userId, subscriberId);
        userService.deleteSubscriber(userId, subscriberId);
        log.info("Ответ отправлен на запрос DELETE /users/{}/subscriptions/{}", userId, subscriberId);
    }

    @DeleteMapping("/{userId}/subscriptions/{subscribedId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(@PathVariable Long userId, @PathVariable Long subscribedId) {
        log.info("Получен запрос DELETE /users/{}/subscriptions/{}", userId, subscribedId);
        userService.unsubscribe(userId, subscribedId);
        log.info("Ответ отправлен на запрос DELETE /users/{}/subscriptions/{}", userId, subscribedId);
    }
}
