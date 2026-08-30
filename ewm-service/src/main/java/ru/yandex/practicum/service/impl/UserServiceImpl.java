package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exeptions.NotFoundException;
import ru.yandex.practicum.model.dto.NewUserRequest;
import ru.yandex.practicum.model.dto.UserDto;
import ru.yandex.practicum.model.mapper.Mapper;
import ru.yandex.practicum.repository.UserRepository;
import ru.yandex.practicum.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Transactional
    @Override
    public UserDto createUser(NewUserRequest dto) {
        return Mapper.toUserDto(repository.save(Mapper.fromNewUserDto(dto)));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return  repository.findByIdIn(ids, pageable).stream().map(Mapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long userId) {
        return Mapper.toUserDto(repository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден")));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        repository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        repository.deleteById(userId);
    }

}
