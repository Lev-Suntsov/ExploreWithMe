package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exeptions.ConflictException;
import ru.yandex.practicum.exeptions.NotFoundException;
import ru.yandex.practicum.model.Category;
import ru.yandex.practicum.model.Event;
import ru.yandex.practicum.model.RequestStatus;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.model.dto.*;
import ru.yandex.practicum.model.mapper.Mapper;
import ru.yandex.practicum.model.state.EventState;
import ru.yandex.practicum.repository.CategoryRepository;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.repository.RequestRepository;
import ru.yandex.practicum.repository.UserRepository;
import ru.yandex.practicum.service.EventService;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;


    @Override
    @Transactional
    public EventDto createEvent(Long userId, NewEventDto dto) throws BadRequestException {
        if (dto.getParticipantLimit() != null && dto.getParticipantLimit() < 0) {
            throw new BadRequestException("Participant limit cannot be negative");
        }
        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictException("Событие должно быть не ранее чем за 1 час от даты публикации");
        }
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        Category category = categoryRepository.findById(dto.getCategory()).orElseThrow(() -> new NotFoundException("категория с id" + dto.getCategory() + " не найдена"));
        Event event = new Event();
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setTitle(dto.getTitle());
        event.setAnnotation(dto.getAnnotation());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setLocation(dto.getLocation());
        event.setPaid(dto.isPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.isRequestModeration());
        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now());

        Event saved = eventRepository.save(event);

        return Mapper.toEventDto(saved);
    }

    @Override
    @Transactional
    public EventDto updateEvent(
            Long userId,
            Long eventId,
            UpdateEventUserRequest dto) {

        Event event = eventRepository.getById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event с id = " + eventId + " не найден");
        }

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException(
                    "Изменять можно только события в состоянии PENDING или CANCELED"
            );
        }


        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case "SEND_TO_REVIEW":
                    event.setState(EventState.PENDING);
                    break;
                case "CANCEL_REVIEW":
                    event.setState(EventState.CANCELED);
                    break;
                default:
                    throw new ConflictException("state указан не верно");
            }
        }

        return Mapper.toEventDto(eventRepository.save(event));
    }

    public EventDto getUserEvent(Long userId, Long eventId) {
        return Mapper.toEventDto(eventRepository.findByIdAndInitiatorId(userId, eventId));
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        userRepository.getById(userId);

        PageRequest pageRequest = PageRequest.of(from / size, size);

        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest);

        List<EventDto> eventsDto = events.stream().map(Mapper::toEventDto).collect(Collectors.toList());

        return eventsDto.stream().map(Mapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventDto findPublicEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event с id=" + eventId + "не найден"));

        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new NotFoundException("Event с id=" + eventId + " не является публичной");
        }
        return Mapper.toEventDto(eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event с id=" + eventId + "не найден")));
    }

    @Override
    public List<EventDto> findAdminEvents(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size
    ) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events;

        if (hasFilters(users, states, categories, rangeStart, rangeEnd)) {
            Specification<Event> spec = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(root.get("initiator").get("id").in(users));
                predicates.add(root.get("state").in(states));
                predicates.add(root.get("category").get("id").in(categories));
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
                return cb.and(predicates.toArray(new Predicate[0]));
            };

            events = eventRepository.findAll(spec, pageable).getContent();
        } else {
            events = eventRepository.findAll(pageable).getContent();
        }

        return events.stream()
                .map(e -> Mapper.toEventDto(e))
                .collect(Collectors.toList());
    }

    private boolean hasFilters(List<Long> users, List<EventState> states,
                               List<Long> categories, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd) {
        return !isEmpty(users) || !isEmpty(states) || !isEmpty(categories)
                || rangeStart != null || rangeEnd != null;
    }

    private boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    @Override
    public EventDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest dto) throws BadRequestException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event с id=" + eventId + " не найден"));
        LocalDateTime minEventDate = event.getPublishedOn().plusHours(1);
        if (dto.getEventDate().isBefore(minEventDate)) {
            throw new ConflictException("Событие должно быть не ранее чем за 1 час от даты публикации");
        }

        if (dto.getAnnotation() != null) {
            if ( dto.getAnnotation().length() < 20 || dto.getAnnotation().length() > 2000) {
                throw new BadRequestException("Invalid annotation length"); // Ручная валидация для тестов на длину строк
            }
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            if ( dto.getDescription().length() < 20 || dto.getDescription().length() > 7000) {
                throw new BadRequestException("Invalid description length");
            }
            event.setDescription(dto.getDescription());
        }
        if (dto.getTitle() != null) {
            if (dto.getTitle().length() < 3 || dto.getTitle().length() > 120) {
                throw new BadRequestException("Invalid title length");
            }
            event.setTitle(dto.getTitle());
        }
        if (dto.getParticipantLimit() != null) {
            if (dto.getParticipantLimit() < 0) {
                throw new BadRequestException("Limit cannot be negative");
            }
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }

        switch (dto.getStateAction()) {
            case PUBLISH_EVENT:
                if (!EventState.PENDING.equals(event.getState())) {
                    throw new ConflictException(
                            "Событие должно быть со статусом admin: " + event.getState()
                    );
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;

            case REJECT_EVENT:
                if (EventState.PUBLISHED.equals(event.getState())) {
                    throw new ConflictException(
                            "Event уже опубликована"
                    );
                }
                event.setState(EventState.CANCELED);
                break;
            default:
                throw new ConflictException("Неизвестный статус");
        }

        return Mapper.toEventDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findPublicEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable,
            String sort,
            int from,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                from / size,
                size,
                createSort(sort)
        );

        Specification<Event> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("state"), EventState.PUBLISHED));

            if (text != null && !text.isBlank()) {
                String pattern = "%" + text.toLowerCase() + "%";

                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("annotation")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern)
                ));
            }

            if (categories != null && !categories.isEmpty()) {
                predicates.add(
                        root.get("category").get("id").in(categories)
                );
            }

            if (paid != null) {
                predicates.add(cb.equal(root.get("paid"), paid));
            }

            if (rangeStart != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("eventDate"),
                                rangeStart
                        )
                );
            }

            if (rangeEnd != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("eventDate"),
                                rangeEnd
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Event> page = eventRepository.findAll(specification, pageable);

        List<Event> events = page.getContent();

        return events.stream()
                .filter(event -> !onlyAvailable || isAvailable(event))
                .map(Mapper::toEventDto).map(Mapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    private Sort createSort(String sort) {
        if ("VIEWS".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.DESC, "views");
        }

        return Sort.by(Sort.Direction.ASC, "eventDate");
    }

    private boolean isAvailable(Event event) {
        long confirmedRequests = requestRepository.countByEventIdAndStatus(
                event.getId(),
                RequestStatus.CONFIRMED
        );

        return event.getParticipantLimit() == 0
                || confirmedRequests < event.getParticipantLimit();
    }
}