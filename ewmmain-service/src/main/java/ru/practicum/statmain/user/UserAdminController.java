package ru.practicum.statmain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statmain.user.dto.UserCreateRequest;
import ru.practicum.statmain.user.dto.UserResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserAdminController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse addUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("Получен запрос POST /admin/users body={}", request);
        UserResponse response = userService.addUser(request);
        log.info("Ответ отправлен на запрос POST /admin/users body={}", response);
        return response;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getUsers(@RequestParam(required = false) List<Long> ids,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /admin/users");
        List<UserResponse> response = userService.getUsers(ids, from, size);
        log.info("Ответ отправлен на запрос GET /admin/users body={}", response.size());
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("Получен запрос DELETE /admin/users/{}", id);
        userService.deleteUser(id);
        log.info("Ответ отправлен на запрос DELETE /admin/users/{}", id);
    }

}
