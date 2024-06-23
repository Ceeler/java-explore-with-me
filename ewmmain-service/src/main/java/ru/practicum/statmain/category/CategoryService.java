package ru.practicum.statmain.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.statmain.category.dto.CategoryCreateRequest;
import ru.practicum.statmain.category.dto.CategoryDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDto addCategory(CategoryCreateRequest request) {
        Category category = CategoryMapper.toEntity(request);
        categoryRepository.save(category);
        return CategoryMapper.toResponse(category);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    public CategoryDto updateCategory(Integer id, CategoryCreateRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(request.getName());
        categoryRepository.save(category);
        return CategoryMapper.toResponse(category);
    }

    public CategoryDto getCategory(Integer id) {
        return CategoryMapper.toResponse(categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Category not found")));
    }

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return CategoryMapper.toResponse(categoryRepository.findAll(PageRequest.of(from / size, size)).getContent());
    }

}
