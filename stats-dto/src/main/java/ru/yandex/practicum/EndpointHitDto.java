package ru.yandex.practicum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EndpointHitDto {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
