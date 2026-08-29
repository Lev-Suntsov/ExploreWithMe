package ru.yandex.practicum.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.yandex.practicum.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class NewEventDto {

    @NotBlank(message = "Аннотация не должна быть пустой")
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull(message = "Нужно указать категорию")
    private Long category;

    @NotBlank(message = "Описание не должно быть пустым")
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull(message = "Нужно указать дату события")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Нужно указать место проведения")
    private Location location;

    private boolean paid = false;

    private int participantLimit = 0;

    private boolean requestModeration = true;

    @NotBlank(message = "Название не должно быть пустым")
    @Size(min = 3, max = 120)
    private String title;
}
