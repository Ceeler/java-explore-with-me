package ru.practicum.statmain.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statmain.category.CategoryService;
import ru.practicum.statmain.category.dto.CategoryCreateRequest;
import ru.practicum.statmain.category.dto.CategoryDto;
import ru.practicum.statmain.compilation.CompilationService;
import ru.practicum.statmain.compilation.dto.CompilationCreateRequest;
import ru.practicum.statmain.compilation.dto.CompilationDto;
import ru.practicum.statmain.compilation.dto.CompilationPatchRequest;
import ru.practicum.statmain.event.EventService;
import ru.practicum.statmain.event.dto.EventAdminPatchDto;
import ru.practicum.statmain.event.dto.EventFullDto;
import ru.practicum.statmain.event.enums.State;
import ru.practicum.statmain.exception.BadRequestException;
import ru.practicum.statmain.user.UserService;
import ru.practicum.statmain.user.dto.UserCreateRequest;
import ru.practicum.statmain.user.dto.UserResponse;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;

    private final CategoryService categoryService;

    private final CompilationService compilationService;

    private final EventService eventService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse addUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("Получен запрос POST /admin/users body={}", request);
        UserResponse response = userService.addUser(request);
        log.info("Ответ отправлен на запрос POST /admin/users body={}", response);
        return response;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getUsers(@RequestParam(required = false) List<Long> ids,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /admin/users");
        List<UserResponse> response = userService.getUsers(ids, from, size);
        log.info("Ответ отправлен на запрос GET /admin/users body={}", response.size());
        return response;
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("Получен запрос DELETE /admin/users/{}", id);
        userService.deleteUser(id);
        log.info("Ответ отправлен на запрос DELETE /admin/users/{}", id);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody CategoryCreateRequest request) {
        log.info("Получен запрос POST /admin/categories body={}", request);
        CategoryDto response = categoryService.addCategory(request);
        log.info("Ответ отправлен на запрос POST /admin/categories body={}", response);
        return response;
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer id) {
        log.info("Получен запрос DELETE /admin/categories/{}", id);
        categoryService.deleteCategory(id);
        log.info("Ответ отправлен на запрос DELETE /admin/categories/{}", id);
    }

    @PatchMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@Valid @RequestBody CategoryCreateRequest request, @PathVariable Integer id) {
        log.info("Получен запрос PATCH /admin/categories/{} body={}", id, request);
        CategoryDto response = categoryService.updateCategory(id, request);
        log.info("Ответ отправлен на запрос PATCH /admin/categories/{} body={}", id, response);
        return response;
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEvents(
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

        List<EventFullDto> response = eventService.getAdminEvents(users, categories, states, rangeStart, rangeEnd, from, size);
        log.info("Ответ отправлен на запрос GET /admin/events body={}", response.size());
        return response;
    }

    @PatchMapping("/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto patchEvent(@Valid @RequestBody EventAdminPatchDto request, @PathVariable Long id) {
        log.info("Получен запрос PATCH /admin/events/{} body={}", id, request);
        EventFullDto response = eventService.patchAdminEvent(id, request);
        log.info("Ответ отправлен на запрос PATCH /admin/events/{} body={}", id, response);
        return response;
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody CompilationCreateRequest request) {
        log.info("Получен запрос POST /admin/compilations body={}", request);
        CompilationDto response = compilationService.addCompilation(request);
        log.info("Ответ отправлен на запрос POST /admin/compilations body={}", response);
        return response;
    }

    @DeleteMapping("/compilations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long id) {
        log.info("Получен запрос DELETE /admin/compilations/{}", id);
        compilationService.deleteCompilation(id);
        log.info("Ответ отправлен на запрос DELETE /admin/compilations/{}", id);
    }

    @PatchMapping("/compilations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@Valid @RequestBody CompilationPatchRequest request, @PathVariable Long id) {
        log.info("Получен запрос PATCH /admin/compilations/{} body={}", id, request);
        CompilationDto response = compilationService.patchCompilation(id, request);
        log.info("Ответ отправлен на запрос PATCH /admin/compilations/{} body={}", id, response);
        return response;
    }

}
