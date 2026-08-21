import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.client.StatsClient;

@Configuration
public class ClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public StatsClient statsClient(
            RestTemplateBuilder restTemplate,
            @Value("${stats-server.url}") String statsServerUrl) {

        return new StatsClient(statsServerUrl, restTemplate);
    }
}