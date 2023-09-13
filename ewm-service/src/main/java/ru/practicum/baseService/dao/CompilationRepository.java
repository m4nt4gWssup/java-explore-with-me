package ru.practicum.baseService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.baseService.model.Compilation;
import ru.practicum.baseService.util.page.CustomPageRequest;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findAllByPinned(Boolean pinned, CustomPageRequest pageable);
}