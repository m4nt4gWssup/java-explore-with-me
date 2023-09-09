package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.HitViewDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.mapper.HitViewMapper;
import ru.practicum.model.Hit;
import ru.practicum.repository.HitRepository;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class HitSetviceImpl implements HitService {

    private final HitRepository hitRepository;

    @Override
    @Transactional
    public HitDto create(HitDto hitDto) {
        log.info("Создание информации о посещении с данными: {}", hitDto);
        Hit savedHit = hitRepository.save(new Hit(
                hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getTimestamp()));
        log.info("Информация о посещении сохранена с ID: {}", savedHit.getId());
        return HitMapper.toDto(savedHit);
    }

    @Override
    public List<HitViewDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        PageRequest pageable = PageRequest.of(0, 10);
        if (start.isAfter(end) || end.isBefore(start)) {
            throw new ValidationException("Неправильно указано время!");
        }
        if (unique) {
            log.info("Статистика уникальных просмотров получена");
            return HitViewMapper.toDtoList(hitRepository.findUniqueViewStats(start, end, uris, pageable));
        } else {
            log.info("Статистика просмотров получена");
            return HitViewMapper.toDtoList(hitRepository.findViewStats(start, end, uris, pageable));
        }
    }
}
