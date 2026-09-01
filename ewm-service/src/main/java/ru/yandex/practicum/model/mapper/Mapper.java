package ru.yandex.practicum.model.mapper;

import ru.yandex.practicum.model.*;
import ru.yandex.practicum.model.dto.*;

import java.util.Collections;
import java.util.stream.Collectors;

public class Mapper {

    public static UserDto fromEntity(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return dto;
    }

    public static EventDto toEventDto(Event event) {
        if (event == null) return null;
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(event.getCategory() != null ? toCategoryDto(event.getCategory()) : null);
        dto.setEventDate(event.getEventDate());
        dto.setDescription(event.getDescription());
        dto.setInitiator(event.getInitiator() != null ? fromEntity(event.getInitiator()) : null);
        dto.setPaid(event.isPaid());
        dto.setLocation(event.getLocation());
        dto.setTitle(event.getTitle());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setState(event.getState());
        dto.setRequestModeration(event.isRequestModeration());
        dto.setCreatedOn(event.getCreatedOn());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setViews(event.getViews() != null ? event.getViews() : 0L);
        dto.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : 0L);

        return dto;
    }

    public static CategoryDto toCategoryDto(Category category) {
        if (category == null) return null;
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public static Category fromCategoryDtoToEntity(CategoryDto dto) {
        if (dto == null) return null;
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }

    public static ParticipationRequestDto participationRequestDtoFromEntity(ParticipationRequest request) {
        if (request == null) return null;
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(request.getId());

        dto.setRequester(request.getRequester() != null ? request.getRequester().getId() : null);
        dto.setEvent(request.getEvent() != null ? request.getEvent().getId() : null);

        dto.setStatus(request.getStatus());
        dto.setCreated(request.getCreated());
        return dto;
    }

    public static EventShortDto toEventShortDto(EventDto dto) {
        if (dto == null) return null;
        EventShortDto shortDto = new EventShortDto();
        shortDto.setId(dto.getId());
        shortDto.setInitiator(dto.getInitiator());
        shortDto.setCategory(dto.getCategory());
        shortDto.setTitle(dto.getTitle());
        shortDto.setAnnotation(dto.getAnnotation());
        shortDto.setEventDate(dto.getEventDate());
        shortDto.setPaid(dto.isPaid());
        shortDto.setViews(dto.getViews());
        shortDto.setConfirmedRequests(dto.getConfirmedRequests());
        return shortDto;
    }

    public static UserDto toUserDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return dto;
    }

    public static User fromNewUserDto(NewUserRequest request) {
        if (request == null) return null;
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return user;
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        if (compilation == null) return null;
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.isPinned());
        dto.setEvents(compilation.getEvents() != null ?
                compilation.getEvents().stream().map(Mapper::toEventDto).collect(Collectors.toList()) :
                Collections.emptyList());
        return dto;
    }
}
