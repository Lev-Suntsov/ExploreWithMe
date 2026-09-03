package ru.yandex.practicum.service;

import ru.yandex.practicum.model.dto.NewUserRequest;
import ru.yandex.practicum.model.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(NewUserRequest dto);

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto getUser(Long userId);

    void deleteUser(Long userId);
}