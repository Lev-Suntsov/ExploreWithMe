package ru.yandex.practicum.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.dto.CompilationDto;
import ru.yandex.practicum.service.CompilationService; // подставьте ваш точный импорт сервиса

import java.util.List;

@RestController
@RequestMapping("/compilations")
public class PublicCompilationController {

    private final CompilationService compilationService;

    public PublicCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        return compilationService.getCompilationById(compId);
    }
}

