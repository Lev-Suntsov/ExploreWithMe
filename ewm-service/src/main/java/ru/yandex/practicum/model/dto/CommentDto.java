package ru.yandex.practicum.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class CommentDto {
    private Long id;

    @NotBlank(message = "текст не может быть пустым")
    private String text;

    private UserDto commentator;

    private EventDto event;
}
