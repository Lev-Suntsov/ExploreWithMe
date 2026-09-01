package ru.yandex.practicum.service.impl;

import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.yandex.practicum.model.state.EventState;
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

    @Transactional
    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) throws BadRequestException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User с id=" + userId + " не существует"));

        if (eventId == null || eventId == 0) {
            throw new BadRequestException("Incorrectly made request: eventId is missing");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The event initiator cannot create a participation request for their own event.");
        }

        long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        if (event.getParticipantLimit() > 0 && confirmedRequests >= event.getParticipantLimit()) {
            throw new ConflictException("The participant limit for this event has already been reached.");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Cannot participate in an unpublished event.");
        }

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("заявка у этого пользователя уже имеется");
        }

        int confirmedCount = requestRepository.findByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size();
        int participantLimit = event.getParticipantLimit();

        ParticipationRequest request = new ParticipationRequest();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);

// ---> FIX THIS STATUS AUTO-APPROVAL CONDITION <---
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING); // MUST be PENDING if moderated and limited
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
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request с id=" + requestId + " не найден"));

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