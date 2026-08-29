package ru.yandex.practicum.model.mapper;


import ru.yandex.practicum.model.Category;
import ru.yandex.practicum.model.Event;
import ru.yandex.practicum.model.ParticipationRequest;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.model.dto.*;

public class Mapper {
    public static User fromDtoToUser(UserDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        return user;
    }

    public static UserDto fromEntity(User user) {
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return dto;
    }

    public static EventDto toEventDto(Event event) {
        EventDto dto = new EventDto();
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(toCategoryDto(event.getCategory()));
        dto.setEventDate(event.getEventDate());
        dto.setDescription(event.getDescription());
        dto.setInitiator(fromEntity(event.getInitiator()));
        dto.setPaid(event.isPaid());
        dto.setLocation(event.getLocation());
        dto.setTitle(event.getTitle());
        dto.setParticipantLimit(event.getParticipantLimit());
        return dto;
    }

    public static Event fromEventDtoToEntity(EventDto dto) {
        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        event.setCategory(fromCategoryDtoToEntity(dto.getCategory()));
        event.setEventDate(dto.getEventDate());
        event.setDescription(dto.getDescription());
        event.setInitiator(fromDtoToUser(dto.getInitiator()));
        event.setPaid(dto.isPaid());
        event.setLocation(dto.getLocation());
        event.setTitle(dto.getTitle());
        event.setParticipantLimit(dto.getParticipantLimit());
        return event;
    }

    public static CategoryDto toCategoryDto (Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setName(category.getName());
        return dto;
    }

    public static Category fromCategoryDtoToEntity(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public static ParticipationRequestDto participationRequestDtoFromEntity(ParticipationRequest request) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setRequester(fromEntity(request.getRequester()));
        dto.setStatus(request.getStatus());
        dto.setEvent(toEventDto(request.getEvent()));
        dto.setCreated(request.getCreated());
        return dto;
    }

    public static ParticipationRequest participationRequestFromDto(ParticipationRequestDto dto) {
        ParticipationRequest request = new ParticipationRequest();
        request.setCreated(dto.getCreated());
        request.setEvent(fromEventDtoToEntity(dto.getEvent()));
        request.setStatus(dto.getStatus());
        request.setRequester(fromDtoToUser(dto.getRequester()));

        return request;
    }

    public static NewEventDto toNewEventDto(EventDto eventDto){
        NewEventDto newEventDto = new NewEventDto();
        newEventDto.setAnnotation(eventDto.getAnnotation());
        newEventDto.setCategory(newEventDto.getCategory());
        newEventDto.setDescription(eventDto.getDescription());
        newEventDto.setEventDate(eventDto.getEventDate());
        newEventDto.setLocation(eventDto.getLocation());
        newEventDto.setPaid(eventDto.isPaid());
        newEventDto.setTitle(eventDto.getTitle());
        return newEventDto;
    }

    public static EventShortDto toEventShortDto(EventDto dto){
        EventShortDto shortDto = new EventShortDto();
        shortDto.setId(dto.getId());
        shortDto.setInitiator(dto.getInitiator());
        shortDto.setCategory(dto.getCategory());
        shortDto.setTitle(dto.getTitle());
        shortDto.setAnnotation(dto.getAnnotation());
        shortDto.setEventDate(dto.getEventDate());
        shortDto.setPaid(dto.isPaid());
        return shortDto;
    }

    public static UserDto toUserDto(User user){
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return dto;
    }

    public static User fromNewUserDto(NewUserRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
    }
}
