package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.ParticipationRequest;
import ru.yandex.practicum.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {


    List<ParticipationRequest> findByEventId(Long eventId);

    List<ParticipationRequest> findByRequesterId(Long requesterId);

    List<ParticipationRequest> findByEventIdAndStatus(Long eventId, RequestStatus status);

    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requesterId);

    interface ConfirmedCountProjection {
        Long getEventId();
        Long getCount();
    }

    @Query("SELECT r.event.id AS eventId, COUNT(r.id) AS count " +
            "FROM ParticipationRequest r " +
            "WHERE r.event.id IN :eventIds AND r.status = :status " +
            "GROUP BY r.event.id")
    List<ConfirmedCountProjection> countConfirmedRequestsByEventIds(
            @Param("eventIds") List<Long> eventIds,
            @Param("status") RequestStatus status
    );

    Long countByEvent_IdAndStatus(Long eventId, RequestStatus status);

}