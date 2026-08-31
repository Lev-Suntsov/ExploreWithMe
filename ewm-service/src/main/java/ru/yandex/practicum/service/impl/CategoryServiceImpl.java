package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exeptions.ConflictException;
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
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + catId + " не найдена"));
        return Mapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto dto) throws BadRequestException {

        if (categoryRepository.existsByName(dto.getName())) {
            throw new ConflictException("Категория с данным именем уже существует");
        }

        Category category = new Category();
        category.setName(dto.getName());

        Category saved = categoryRepository.save(category);
        return Mapper.toCategoryDto(saved);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto dto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("категория с id=" + catId + " не найдена"));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            if (!category.getName().equals(dto.getName())
                    && categoryRepository.existsByName(dto.getName())) {
                throw new ConflictException("категория с данным именем уже используется");
            }
            category.setName(dto.getName());
        }

        Category updated = categoryRepository.save(category);
        return Mapper.toCategoryDto(updated);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("категория с id=" + catId + " не найдена");
        }

        long count = categoryRepository.countEventsByCategoryId(catId);
        if (count > 0) {
            throw new ConflictException("категория не пустая");
        }

        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getCompilationsOrCategories(int from, int size) {
        if (size <= 0) {
            size = 10;
        }
        int pageNumber = from / size;

        var page = categoryRepository.findAll(PageRequest.of(pageNumber, size));
        return page.stream()
                .map(Mapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}
