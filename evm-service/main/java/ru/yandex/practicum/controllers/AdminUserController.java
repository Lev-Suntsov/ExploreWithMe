package ru.yandex.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.dto.NewUserRequest;
import ru.yandex.practicum.model.dto.UserDto;
import ru.yandex.practicum.service.impl.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserServiceImpl userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerUser(@RequestBody @Valid NewUserRequest dto) {
        return userService.createUser(dto);
    }

    @GetMapping
    public List<UserDto> getUsers(
            @NotNull(message = "id не могут быть пустыми") @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.getUsers(ids, from, size);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@NotNull(message = "id не может быть пустым") @PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@NotNull(message = "id не может быть пустым") @PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
