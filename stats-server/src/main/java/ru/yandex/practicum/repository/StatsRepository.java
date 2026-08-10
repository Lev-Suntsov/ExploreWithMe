package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.model.EndpointHit;
import ru.yandex.practicum.model.StatsRow;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select e.app, e.uri, COUNT(*)  from EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND e.uri IN (:uris) " +
            "GROUP BY e.app, e.uri")
    List<StatsRow> findAllStats(LocalDateTime start,
                                LocalDateTime end,
                                List<String> uris);

    @Query("select e.app, e.uri,  COUNT(DISTINCT e.ip) from  EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND e.uri IN (:uris) " +
            "GROUP BY e.app, e.uri")
    List<StatsRow> findUniqueStats(LocalDateTime start,
                                    LocalDateTime end,
                                    List<String> uris);
}
