package ru.yandex.practicum.model.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    private String title;
    private boolean pinned;
    private List<EventDto> events;
}

