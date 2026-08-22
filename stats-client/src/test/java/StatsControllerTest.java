import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.ViewStats;
import ru.yandex.practicum.client.StatsClient;
import ru.yandex.practicum.client.controller.StatsController;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsControllerTest {

    @Mock
    private StatsClient client;
    private StatsController controller;

    @BeforeEach
    void setUp() {
        controller = new StatsController(client);
    }

    @Test
    void shouldFindAllStats() {
        LocalDateTime start =
                LocalDateTime.of(2026, 8, 1, 0, 0);

        LocalDateTime end =
                LocalDateTime.of(2026, 8, 8, 23, 59);

        List<String> uris = List.of("/events/1");

        List<ViewStats> expected = List.of();

        when(client.findAllStats(start, end, uris))
                .thenReturn(expected);

        List<ViewStats> actual =
                controller.findAllStats(start, end, uris, false);
        assertEquals(expected, actual);

        verify(client).findAllStats(start, end, uris);
    }

    @Test
    void shouldFindUniqueStats() {
        LocalDateTime start = LocalDateTime.of(2026, 8, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 8, 8, 23, 59);

        List<String> uris = List.of("/events/1");

        ViewStats viewStats = new ViewStats(
                "ewm-main-service",
                "/events/1",
                3L
        );

        List<ViewStats> expected = List.of(viewStats);

        when(client.findUniqueStats(start, end, uris))
                .thenReturn(expected);

        List<ViewStats> actual =
                controller.findUniqueStats(start, end, uris);

        assertEquals(expected, actual);

        verify(client).findUniqueStats(start, end, uris);
    }
}
