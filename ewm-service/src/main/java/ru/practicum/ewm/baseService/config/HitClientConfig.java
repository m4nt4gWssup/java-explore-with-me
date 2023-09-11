package ru.practicum.ewm.baseService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.HitClient;

@Configuration
public class HitClientConfig {

    @Value("${stats-service.url}")
    private String url;

    @Bean
    HitClient statsClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new HitClient(url, builder);
    }
}