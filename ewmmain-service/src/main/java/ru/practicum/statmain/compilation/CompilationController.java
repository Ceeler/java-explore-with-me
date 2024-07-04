package ru.practicum.statmain.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statmain.compilation.dto.CompilationDto;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getAll(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean pinned
    ) {
        log.info("Получен запрос GET /compilations?from={}&size={}&pinned={}", from, size, pinned);
        List<CompilationDto> response = compilationService.getCompilations(from, size, pinned);
        log.info("Ответ отправлен на запрос GET /compilations?from={}&size={}&pinned={} body={}", from, size, pinned, response.size());
        return response;
    }

    @GetMapping("/{id}")
    public CompilationDto getById(@PathVariable Long id) {
        log.info("Получен запрос GET /compilations/{}", id);
        CompilationDto response = compilationService.getCompilation(id);
        log.info("Ответ отправлен на запрос GET /compilations/{} body={}", id, response);
        return response;
    }

}
