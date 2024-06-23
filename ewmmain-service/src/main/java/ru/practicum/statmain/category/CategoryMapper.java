package ru.practicum.statmain.category;

import ru.practicum.statmain.category.dto.CategoryCreateRequest;
import ru.practicum.statmain.category.dto.CategoryDto;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toEntity(CategoryCreateRequest request) {
        return Category.builder()
                .name(request.getName())
                .build();
    }

    public static CategoryDto toResponse(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<CategoryDto> toResponse(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toResponse)
                .collect(Collectors.toList());
    }

}
