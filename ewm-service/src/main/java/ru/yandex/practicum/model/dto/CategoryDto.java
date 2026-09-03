package ru.yandex.practicum.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CategoryDto {
    private Long id;
    @NotBlank(message = "Имя категории не может быть пустым")
    @Size(min = 1, max = 50, message = "Имя категории должно быть от 1 до 50 символов")
    private String name;

}
