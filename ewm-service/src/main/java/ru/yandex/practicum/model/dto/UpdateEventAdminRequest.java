package ru.yandex.practicum.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.yandex.practicum.model.AdminStateAction;
import ru.yandex.practicum.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class UpdateEventAdminRequest {

    @NotBlank(message = "annotation не может быть пустым")
    private String annotation;

    private Long category;
    @NotBlank(message = "description не может быть пустым")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    @NotNull(message = "paid не может быть пустым")
    private Boolean paid;
    @NotNull(message = "participantLimit не может быть пустым")
    private Integer participantLimit;
    @NotNull(message = "requestModeration не может быть пустым")
    private Boolean requestModeration;

    private AdminStateAction stateAction;

    @NotBlank(message = "title не может быть пустым")
    private String title;
}