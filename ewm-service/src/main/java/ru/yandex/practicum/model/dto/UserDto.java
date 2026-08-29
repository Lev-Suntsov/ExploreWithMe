package ru.yandex.practicum.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private Long id;
    @NotBlank(message = "имя не может быть пустым")
    private String name;
    @NotBlank(message = "email не может быть пустым")
    private String email;
}
