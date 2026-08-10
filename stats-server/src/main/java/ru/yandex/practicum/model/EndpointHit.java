package ru.yandex.practicum.model;

import lombok.Data;

import javax.persistence.GenerationType;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "endpointhit")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String app;

    @Column
    private String uri;

    @Column
    private String ip;

    @Column
    private LocalDateTime timestamp;
}
