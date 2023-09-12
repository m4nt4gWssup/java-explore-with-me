package ru.practicum.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HitClient {

    protected final RestTemplate rest;

    public HitClient(RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory("http://stats-server:9090"))
                .build();
    }

    public void create(String ip, String uri, LocalDateTime timeRequest) {
        HitDto statisticDto = new HitDto();
        statisticDto.setIp(ip);
        statisticDto.setUri(uri);
        statisticDto.setApp("ewm-main-service");
        statisticDto.setTimestamp(LocalDateTime.now());
        try {
            rest.postForObject("/hit", statisticDto, HitDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении статистики!" + "\n" + e.getMessage());
        }
    }

    public List<ViewStatsDto> getStatistic(String start, String end, List<String> uris, Boolean unique) {
        try {
            String urisParam = uris.stream()
                    .map(uri -> "uris=" + uri)
                    .collect(Collectors.joining("&"));
            ResponseEntity<String> response = rest.getForEntity("/stats?start=" + start + "&end=" + end + "&"
                    + urisParam + "&unique=" + unique, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.getBody(), new TypeReference<List<ViewStatsDto>>() {
                });
            } else {
                throw new RuntimeException("Ошибка при получении статистики: " + response.getStatusCode() + "\n " +
                        response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении статистики!");
        }
    }
}