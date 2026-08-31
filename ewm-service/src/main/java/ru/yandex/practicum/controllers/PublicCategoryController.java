package ru.yandex.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.dto.CategoryDto;
import ru.yandex.practicum.service.CategoryService;
import ru.yandex.practicum.service.impl.CategoryServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PublicCategoryController  {
    private final CategoryServiceImpl categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(
            @RequestParam(defaultValue = "0") int from,      // Должно быть строго 0
            @RequestParam(defaultValue = "10") int size       // Должно быть строго 10
    ) {
        return categoryService.getCompilationsOrCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) { // Путь строго разделен через /{catId}
        return categoryService.getCategory(catId);
    }
}
