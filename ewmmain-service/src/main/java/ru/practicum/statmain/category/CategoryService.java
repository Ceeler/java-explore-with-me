package ru.practicum.statmain.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.statmain.category.dto.CategoryCreateRequest;
import ru.practicum.statmain.category.dto.CategoryResponse;
import ru.practicum.statmain.exception.ConflictException;
import ru.practicum.statmain.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse addCategory(CategoryCreateRequest request) {
        checkName(request.getName());
        Category category = CategoryMapper.toEntity(request);
        categoryRepository.save(category);
        return CategoryMapper.toResponse(category);
    }

    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        if (!category.getEvents().isEmpty()) {
            throw new ConflictException("Category has events");
        }
        categoryRepository.deleteById(id);
    }

    public CategoryResponse updateCategory(Integer id, CategoryCreateRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        if (!request.getName().equals(category.getName())) {
            checkName(request.getName());
        category.setName(request.getName());
        categoryRepository.save(category);
        }
        return CategoryMapper.toResponse(category);
    }

    public CategoryResponse getCategory(Integer id) {
        return CategoryMapper.toResponse(categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category not found")));
    }

    public List<CategoryResponse> getCategories(Integer from, Integer size) {
        return CategoryMapper.toResponse(categoryRepository.findAll(PageRequest.of(from / size, size)).getContent());
    }

    private void checkName(String name) {
        if (categoryRepository.findByName(name).isPresent()) {
            throw new ConflictException("Category with name " + name + " already exists");
        }
    }

}
