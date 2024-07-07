package ru.practicum.statmain.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statmain.category.dto.CategoryResponse;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> getEvents(@RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /categories?from={}&size={}", from, size);
        List<CategoryResponse> response = categoryService.getCategories(from, size);
        log.info("Ответ отправлен на запрос /categories?from={}&size={} body={}", from, size, response.size());
        return response;

    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse getEvent(@PathVariable Integer id) {
        log.info("Получен запрос GET /categories/{}", id);
        CategoryResponse response = categoryService.getCategory(id);
        log.info("Ответ отправлен на запрос /categories/{} body={}", id, response);
        return response;
    }

}
