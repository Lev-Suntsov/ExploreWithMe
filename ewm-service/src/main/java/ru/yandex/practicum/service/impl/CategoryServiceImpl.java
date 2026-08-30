package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exeptions.NotFoundException;
import ru.yandex.practicum.model.Category;
import ru.yandex.practicum.model.dto.CategoryDto;
import ru.yandex.practicum.model.dto.NewCategoryDto;
import ru.yandex.practicum.model.mapper.Mapper;
import ru.yandex.practicum.repository.CategoryRepository;
import ru.yandex.practicum.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(NewCategoryDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }

        if (categoryRepository.existsByName(dto.getName())) {
            throw new IllegalStateException("Category with this name already exists");
        }

        Category category = new Category();
        category.setName(dto.getName());

        Category saved = categoryRepository.save(category);

        return Mapper.toCategoryDto(saved);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto dto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " not found"));
        if (dto.getName() != null && !dto.getName().isBlank()) {
            if (!category.getName().equals(dto.getName())
                    && categoryRepository.existsByName(dto.getName())) {
                throw new IllegalStateException("Category with this name already exists");
            }
            category.setName(dto.getName());
        }

        Category updated = categoryRepository.save(category);
        return Mapper.toCategoryDto(updated);
    }

    @Override
    public void deleteCategory(Long catId) {
        long count = categoryRepository.countEventsByCategoryId(catId);
        if (count > 0) {
            throw new IllegalStateException("Category is not empty");
        }

        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        var page = categoryRepository.findAll(PageRequest.of(from / size, size));
        return page.stream()
                .map(Mapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " not found"));
        return Mapper.toCategoryDto(category);
    }

    private Category getCategoryEntity(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() ->
                        new IllegalStateException("Category with id=" + catId + " not found"));
    }
}