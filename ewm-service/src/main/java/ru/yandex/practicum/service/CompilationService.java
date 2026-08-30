package ru.yandex.practicum.service;

import ru.yandex.practicum.model.dto.CompilationDto;
import ru.yandex.practicum.model.dto.NewCompilationDto;
import ru.yandex.practicum.model.dto.UpdateCompilationRequest;
import java.util.List;

public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);
    void deleteCompilation(Long compId);
    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateRequest);
    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);
    CompilationDto getCompilationById(Long compId);
}

