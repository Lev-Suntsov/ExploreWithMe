package ru.yandex.practicum.model;

import lombok.NoArgsConstructor;

import javax.persistence.GenerationType;
import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "endpointhit")
public class EndpointHit {
    public EndpointHit(String app, String uri, String ip, LocalDateTime timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String app;

    @Column
    private String uri;

    @Column
    private String ip;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getIp() {
        return ip;
    }


    public String getUri() {
        return uri;
    }


    public String getApp() {
        return app;
    }

    @Column
    private LocalDateTime timestamp;


}
