package ru.practicum.publicService.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.baseService.dao.CompilationRepository;
import ru.practicum.baseService.dto.Compilation.CompilationDto;
import ru.practicum.baseService.exception.NotFoundException;
import ru.practicum.baseService.mapper.CompilationMapper;
import ru.practicum.baseService.model.Compilation;
import ru.practicum.baseService.util.page.CustomPageRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PublicCompilationsServiceImpl implements PublicCompilationsService {
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        CustomPageRequest pageable = new CustomPageRequest(from, size,
                Sort.by(Sort.Direction.ASC, "id"));
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).toList();
        }

        log.info("Получен список всех подборок:");
        return CompilationMapper.toDtoList(compilations);
    }

    @Override
    public CompilationDto get(Long comId) {
        final Compilation compilation = compilationRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException(String.format("Подборка не найдена с id = %s", comId)));
        log.info("Получена подборка: {}", compilation.getTitle());
        return CompilationMapper.toDto(compilation);
    }
}