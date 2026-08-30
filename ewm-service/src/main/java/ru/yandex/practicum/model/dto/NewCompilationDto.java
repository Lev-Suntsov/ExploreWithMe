package ru.yandex.practicum.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class NewCompilationDto {

    private List<Long> events;

    private Boolean pinned;

    @NotBlank(message = "Заголовок подборки не может быть пустым")
    @Size(min = 1, max = 50, message = "Размер заголовка должен быть от 1 до 50 символов")
    private String title;
}

