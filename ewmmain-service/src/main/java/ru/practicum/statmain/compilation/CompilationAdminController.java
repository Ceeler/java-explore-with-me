package ru.practicum.statmain.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statmain.compilation.dto.CompilationCreateRequest;
import ru.practicum.statmain.compilation.dto.CompilationPatchRequest;
import ru.practicum.statmain.compilation.dto.CompilationResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponse addCompilation(@Valid @RequestBody CompilationCreateRequest request) {
        log.info("Получен запрос POST /admin/compilations body={}", request);
        CompilationResponse response = compilationService.addCompilation(request);
        log.info("Ответ отправлен на запрос POST /admin/compilations body={}", response);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long id) {
        log.info("Получен запрос DELETE /admin/compilations/{}", id);
        compilationService.deleteCompilation(id);
        log.info("Ответ отправлен на запрос DELETE /admin/compilations/{}", id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationResponse updateCompilation(@Valid @RequestBody CompilationPatchRequest request, @PathVariable Long id) {
        log.info("Получен запрос PATCH /admin/compilations/{} body={}", id, request);
        CompilationResponse response = compilationService.patchCompilation(id, request);
        log.info("Ответ отправлен на запрос PATCH /admin/compilations/{} body={}", id, response);
        return response;
    }

}
