package ru.yandex.practicum.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exeptions.ConflictException;
import ru.yandex.practicum.exeptions.NotFoundException;
import ru.yandex.practicum.model.Compilation;
import ru.yandex.practicum.model.Event;
import ru.yandex.practicum.model.dto.CompilationDto;
import ru.yandex.practicum.model.dto.NewCompilationDto;
import ru.yandex.practicum.model.dto.UpdateCompilationRequest;
import ru.yandex.practicum.model.mapper.Mapper;
import ru.yandex.practicum.repository.CompilationRepository;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.service.CompilationService;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned() != null && newCompilationDto.getPinned());

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            compilation.setEvents(new HashSet<>(events));
        } else {
            compilation.setEvents(new HashSet<>());
        }

        try {
            Compilation saved = compilationRepository.save(compilation);
            return Mapper.toCompilationDto(saved);
        } catch (Exception e) {
            throw new ConflictException("Compilation с данным именем уже зарегистрировано");
        }
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation с id=" + compId + " не найдена");
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation с id=" + compId + " не найдена"));

        if (updateRequest.getTitle() != null) {
            compilation.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getPinned() != null) {
            compilation.setPinned(updateRequest.getPinned());
        }
        if (updateRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateRequest.getEvents());
            compilation.setEvents(new HashSet<>(events));
        }

        try {
            Compilation updated = compilationRepository.save(compilation);
            return Mapper.toCompilationDto(updated);
        } catch (Exception e) {
            throw new ConflictException("Compilation с данным именем уже существует");
        }
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations;

        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).getContent();
        }

        return compilations.stream()
                .map(Mapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation с id =" + compId + " не найдена"));
        return Mapper.toCompilationDto(compilation);
    }
}

