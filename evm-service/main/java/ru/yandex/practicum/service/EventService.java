package ru.yandex.practicum.service;

import ru.yandex.practicum.model.dto.*;
import ru.yandex.practicum.model.state.EventState;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventDto createEvent(Long userId, NewEventDto dto);

    EventDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest dto);

    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventDto getUserEvent(Long userId, Long eventId);

    List<EventShortDto> findPublicEvents(
             String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable,
            String sort,
            int from,
            int size
    );

    EventDto findPublicEvent(Long eventId);

    List<EventDto> findAdminEvents(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size
    );

    EventDto updateEventByAdmin(
            Long eventId,
            UpdateEventAdminRequest dto
    );
}