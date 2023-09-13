package ru.practicum.adminService.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.baseService.dao.CompilationRepository;
import ru.practicum.baseService.dao.EventRepository;
import ru.practicum.baseService.dto.Compilation.CompilationDto;
import ru.practicum.baseService.dto.Compilation.NewCompilationDto;
import ru.practicum.baseService.dto.Compilation.UpdateCompilationRequest;
import ru.practicum.baseService.exception.ConflictException;
import ru.practicum.baseService.exception.NotFoundException;
import ru.practicum.baseService.mapper.CompilationMapper;
import ru.practicum.baseService.model.Compilation;
import ru.practicum.baseService.model.Event;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        compilation.setEvents(findEvents(newCompilationDto.getEvents()));
        try {
            compilation = compilationRepository.save(compilation);
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка при создании подборки: {}", e.getMessage(), e);
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Добавлена подборка: {}", compilation.getTitle());
        return CompilationMapper.toDto(compilation);
    }

    @Transactional
    @Override
    public void delete(Long compId) {
        if (compilationRepository.existsById(compId)) {
            log.info("Удалена подборка с id = {}", compId);
            compilationRepository.deleteById(compId);
        }
    }

    @Transactional
    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest dto) {
        Compilation compilationTarget = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Подборка с id = %s не найдена", compId)));

        BeanUtils.copyProperties(dto, compilationTarget, "events", "pinned", "title");

        compilationTarget.setEvents(findEvents(dto.getEvents()));

        try {
            compilationRepository.flush();
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка при обновлении подборки: {}", e.getMessage(), e);
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Обновлена подборка: {}", compilationTarget.getTitle());
        return CompilationMapper.toDto(compilationTarget);
    }

    private Set<Event> findEvents(Set<Long> eventsId) {
        if (eventsId == null) {
            return Set.of();
        }
        return eventRepository.findAllByIdIn(eventsId);
    }
}