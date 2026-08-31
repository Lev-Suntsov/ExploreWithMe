package ru.yandex.practicum.service;

import org.apache.coyote.BadRequestException;
import ru.yandex.practicum.model.dto.CategoryDto;
import ru.yandex.practicum.model.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto dto) throws BadRequestException;

    CategoryDto updateCategory(Long catId, CategoryDto dto);

    void deleteCategory(Long catId);

    List<CategoryDto> getCompilationsOrCategories(int from, int size);

    CategoryDto getCategory(Long catId);
}
