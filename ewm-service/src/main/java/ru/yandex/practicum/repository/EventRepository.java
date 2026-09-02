package ru.yandex.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.model.Event;
import ru.yandex.practicum.model.RequestStatus;
import ru.yandex.practicum.model.state.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.id = :eventId " +
            "AND e.initiator.id = :userId")
    Event findByIdAndInitiatorId(
            @Param("eventId") Long eventId,
            @Param("userId") Long userId
    );

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.initiator.id = :userId " +
            "ORDER BY e.id ASC")
    List<Event> findAllByInitiatorId(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("SELECT e FROM Event e " +
            "WHERE e.state = :state " +
            "AND (:text IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (cast(:rangeStart as date) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd as date) IS NULL OR e.eventDate <= :rangeEnd)")
    Page<Event> findPublicEvents(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("state") EventState state,
            Pageable pageable
    );
}
