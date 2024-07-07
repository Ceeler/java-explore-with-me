package ru.practicum.statmain.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statmain.event.dto.EventAdminPatchRequest;
import ru.practicum.statmain.event.dto.EventFullResponse;
import ru.practicum.statmain.event.enums.State;
import ru.practicum.statmain.exception.BadRequestException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullResponse> getEvents(
            @RequestParam(value = "users", required = false) List<Long> users,
            @RequestParam(value = "categories", required = false) List<Integer> categories,
            @RequestParam(value = "states", required = false) List<String> statesStr,
            @RequestParam(value = "rangeStart", required = false) LocalDateTime rangeStart,
            @RequestParam(value = "rangeEnd", required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Получен запрос GET /admin/events?users={}&categories={}&states={}&rangeStart={}&rangeEnd={}&from={}&size={}", users, categories, statesStr, rangeStart, rangeEnd, from, size);
        List<State> states = null;
        if (statesStr != null) {
            states = statesStr.stream().map(s -> State.from(s)
                            .orElseThrow(() -> new BadRequestException("Unsupported state")))
                    .collect(Collectors.toList());
        }

        List<EventFullResponse> response = eventService.getAdminEvents(users, categories, states, rangeStart, rangeEnd, from, size);
        log.info("Ответ отправлен на запрос GET /admin/events body={}", response.size());
        return response;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullResponse patchEvent(@Valid @RequestBody EventAdminPatchRequest request, @PathVariable Long id) {
        log.info("Получен запрос PATCH /admin/events/{} body={}", id, request);
        EventFullResponse response = eventService.patchAdminEvent(id, request);
        log.info("Ответ отправлен на запрос PATCH /admin/events/{} body={}", id, response);
        return response;
    }

}
