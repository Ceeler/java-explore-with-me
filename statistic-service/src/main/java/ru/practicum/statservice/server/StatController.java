package ru.practicum.statservice.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statservice.dto.EndpointHit;
import ru.practicum.statservice.dto.EndpointStat;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatController {

    private final StatService statService;

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<EndpointStat> getStat(
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end") LocalDateTime end,
            @RequestParam(value = "uris", defaultValue = "") List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") Boolean unique
            ) {
        log.info("GET /stats?start={}&end={}&unique={}", start, end, unique);
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start must be before end");
        }
        List<EndpointStat> response = statService.getStats(start, end, uris, unique);
        log.info("GET /stats?start={}&end={}&unique={}  =>  {}", start, end, unique, response);
        return response;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addStat(@RequestBody @Valid EndpointHit dto) {
        log.info("POST /hit => {}", dto);
        statService.addStat(dto);
        log.info("POST /hit Created 201");
    }
}
