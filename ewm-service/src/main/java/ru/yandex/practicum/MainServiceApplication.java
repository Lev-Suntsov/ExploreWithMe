package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.yandex.practicum")
@EntityScan(basePackages = "ru.yandex.practicum")            
public class MainServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }
}

