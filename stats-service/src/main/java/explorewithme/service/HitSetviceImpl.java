package explorewithme.service;

import explorewithme.dto.HitDto;
import explorewithme.dto.HitViewDto;
import explorewithme.mapper.HitMapper;
import explorewithme.mapper.HitViewMapper;
import explorewithme.model.Hit;
import explorewithme.repository.HitRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
        return HitMapper.toDto(hitRepository.save(new Hit(
                hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getTimestamp())));
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
