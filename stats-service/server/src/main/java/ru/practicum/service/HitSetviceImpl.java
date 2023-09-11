package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.repository.HitRepository;

import javax.transaction.Transactional;
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
        return HitMapper.toDto(hitRepository.save(HitMapper.toHit(hitDto)));
    }

    @Override
    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        PageRequest pageable = PageRequest.of(0, 10);
        if (unique) {
            return ViewStatsMapper.toDtoList(hitRepository.findUniqueViewStats(start, end, uris, pageable));
        } else {
            return ViewStatsMapper.toDtoList(hitRepository.findViewStats(start, end, uris, pageable));
        }
    }
}