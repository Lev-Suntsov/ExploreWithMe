package ru.yandex.practicum.service.impl;

import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exeptions.ConflictException;
import ru.yandex.practicum.exeptions.NotFoundException;
import ru.yandex.practicum.model.Event;
import ru.yandex.practicum.model.EventRequestStatusUpdateResult;
import ru.yandex.practicum.model.ParticipationRequest;
import ru.yandex.practicum.model.RequestStatus;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.model.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.model.dto.ParticipationRequestDto;
import ru.yandex.practicum.model.mapper.Mapper;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.repository.RequestRepository;
import ru.yandex.practicum.repository.UserRepository;
import ru.yandex.practicum.service.RequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User с id=" + userId + " не существует"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Нельзя создавать заявку на своё событие");
        }

        if (!event.getState().equals(ru.yandex.practicum.model.EventState.PUBLISHED)) {
            throw new ConflictException("Событие должно быть опубликовано");
        }

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("заявка у этого пользователя уже имеется");
        }

        int confirmedCount = requestRepository.findByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size();
        int participantLimit = event.getParticipantLimit();

        ParticipationRequest request = new ParticipationRequest();
        request.setRequester(user);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());

        if (participantLimit == 0 || confirmedCount < participantLimit) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        ParticipationRequest saved = requestRepository.save(request);

        return Mapper.participationRequestDtoFromEntity(saved);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User с id=" + userId + " не найден");
        }

        List<ParticipationRequest> requests = requestRepository.findByRequesterId(userId);

        return requests.stream()
                .map(Mapper::participationRequestDtoFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request с id=" + requestId + " не найден"));

        if (request.getStatus() == RequestStatus.REJECTED) {
            throw new ConflictException("Можно отменять только PENDING или CONFIRMED");
        }

        if (request.getStatus() == RequestStatus.CANCELED) {
            throw new ConflictException("Реквест уже закрыт");
        }
        request.setStatus(RequestStatus.CANCELED);

        ParticipationRequest saved = requestRepository.save(request);

        return Mapper.participationRequestDtoFromEntity(saved);
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        List<ParticipationRequest> requests = requestRepository.findByEventId(eventId);

        return requests.stream()
                .map(Mapper::participationRequestDtoFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestStatus(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest dto
    ) throws BadRequestException {
        // 1. Проверить, что событие существует
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        // 2. Проверить, что userId — владелец события
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        // 3. Проверить, что статус заявки имеет допустимое значение
        RequestStatus status = dto.getStatus();
        if (status != RequestStatus.CONFIRMED && status != RequestStatus.REJECTED) {
            throw new BadRequestException("Invalid request status");
        }

        // 4. Найти все заявки по id из requestIds
        List<ParticipationRequest> requests = requestRepository.findAllById(dto.getRequestIds());

        // 5. Проверить, что все заявки относятся к этому событию
        for (ParticipationRequest request : requests) {
            if (!request.getEvent().getId().equals(eventId)) {
                throw new NotFoundException("Request with id=" + request.getId() + " does not belong to event " + eventId);
            }
        }

        // 6. Если статус CONFIRMED, проверить лимит участников
        if (status == RequestStatus.CONFIRMED) {
            int confirmedCount = requestRepository.findByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size();
            int participantLimit = event.getParticipantLimit();

            // participantLimit == 0 означает безлимитное участие
            if (participantLimit > 0 && confirmedCount + requests.size() > participantLimit) {
                throw new ConflictException("The participant limit has been reached");
            }
        }

        // 7. Обновить статус заявок
        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            // Можно менять только заявки в статусе PENDING
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("Request must have status PENDING");
            }

            request.setStatus(status);
            requestRepository.save(request);

            if (status == RequestStatus.CONFIRMED) {
                confirmedRequests.add(request);
            } else {
                rejectedRequests.add(request);
            }
        }

        // 8. Сформировать результат
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(confirmedRequests.stream()
                .map(Mapper::participationRequestDtoFromEntity)
                .collect(Collectors.toList()));
        result.setRejectedRequests(rejectedRequests.stream()
                .map(Mapper::participationRequestDtoFromEntity)
                .collect(Collectors.toList()));

        return result;
    }
}