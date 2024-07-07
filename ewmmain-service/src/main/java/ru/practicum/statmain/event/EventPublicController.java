package ru.practicum.statmain.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statmain.event.dto.EventFullResponse;
import ru.practicum.statmain.event.dto.EventShortResponse;
import ru.practicum.statmain.event.enums.Sorting;
import ru.practicum.statmain.exception.BadRequestException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortResponse> getEvents(@RequestParam(required = false) String text,
                                              @RequestParam(required = false) List<Integer> categories,
                                              @RequestParam(required = false) Boolean paid,
                                              @RequestParam(required = false) LocalDateTime rangeStart,
                                              @RequestParam(required = false) LocalDateTime rangeEnd,
                                              @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                              @RequestParam(required = false) String sortStr,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size,
                                              HttpServletRequest request) {
        log.info("Получен запрос GET /events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}" +
                        "&onlyAvailable={}&sort={}&from={}&size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sortStr, from, size);

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestException("Invalid range parameters");
            }
        }

        Sorting sorting = null;

        if (sortStr != null) {
            sorting = Sorting.from(sortStr).orElseThrow(() -> new IllegalArgumentException("Invalid sort parameter"));
        }

        List<EventShortResponse> response = eventService.getEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sorting, from, size, request.getRemoteAddr());

        log.info("Получен запрос GET /events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}" +
                        "&onlyAvailable={}&sort={}&from={}&size={} body={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sorting, from, size, response.size());
        return response;
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullResponse getEvent(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен запрос GET /events/{}", id);
        EventFullResponse event = eventService.getEvent(id, request.getRemoteAddr());
        log.info("Получен запрос GET /events/{} body={}", id, request.getRemoteAddr());
        return event;
    }

}
