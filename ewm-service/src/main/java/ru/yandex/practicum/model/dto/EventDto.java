package ru.yandex.practicum.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.yandex.practicum.model.Location;
import ru.yandex.practicum.model.state.EventState;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventDto {
    private Long id;
    private UserDto initiator;
    private CategoryDto category;
    @NotBlank(message = "title не может быть пустым")
    private String title;
    @NotBlank(message = "annotation не может быть пустым")
    private String annotation;
    @NotBlank(message = "description не может быть пустым")
    private String description;
    private Location location;
    @NotNull(message = "paid не может быть пустым")
    private boolean paid;
    @NotNull(message = "participantLimit не может быть пустым")
    private int participantLimit;
    @NotNull(message = "requestModeration не может быть пустым")
    private boolean requestModeration;
    @NotNull(message = "eventDate не может быть пустым")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "createdOn не может быть пустым")
    private LocalDateTime createdOn;

    private EventState state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
}
