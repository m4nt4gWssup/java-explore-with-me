package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exception.ValidationException;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.repository.HitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public HitDto create(HitDto hitDto) {
        log.info("Создание информации о посещении с данными: {}", hitDto);
        return HitMapper.toDto(hitRepository.save(new Hit(
                hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getTimestamp())));
    }

    @Override
    public List<ViewStatsDto> get(String startFormat, String endFormat, List<String> uris, boolean unique) {
        LocalDateTime start;
        LocalDateTime end;
        try {
            start = LocalDateTime.parse(startFormat, formatter);
            end = LocalDateTime.parse(endFormat, formatter);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Неправильный формат времени!!");
        }

        if (start.isAfter(end) || end.isBefore(start)) {
            throw new ValidationException("Неправильно указано время!");
        }
        if (uris.isEmpty()) {
            if (unique) {
                return hitRepository.getUniqueViewStatsDto(start, end);
            } else {
                return hitRepository.getNotUniqueViewStatsDto(start, end);
            }
        } else {
            if (unique) {
                return hitRepository.getUniqueViewStatsDto(start, end, uris);
            } else {
                return hitRepository.getNotUniqueViewStatsDto(start, end, uris);
            }
        }
    }
}