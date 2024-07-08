package ru.practicum.statmain.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statmain.category.dto.CategoryCreateRequest;
import ru.practicum.statmain.category.dto.CategoryResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategory(@Valid @RequestBody CategoryCreateRequest request) {
        log.info("Получен запрос POST /admin/categories body={}", request);
        CategoryResponse response = categoryService.addCategory(request);
        log.info("Ответ отправлен на запрос POST /admin/categories body={}", response);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer id) {
        log.info("Получен запрос DELETE /admin/categories/{}", id);
        categoryService.deleteCategory(id);
        log.info("Ответ отправлен на запрос DELETE /admin/categories/{}", id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse updateCategory(@Valid @RequestBody CategoryCreateRequest request, @PathVariable Integer id) {
        log.info("Получен запрос PATCH /admin/categories/{} body={}", id, request);
        CategoryResponse response = categoryService.updateCategory(id, request);
        log.info("Ответ отправлен на запрос PATCH /admin/categories/{} body={}", id, response);
        return response;
    }
}