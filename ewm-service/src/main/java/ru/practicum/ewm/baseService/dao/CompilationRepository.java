package ru.practicum.ewm.baseService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.baseService.model.Compilation;
import ru.practicum.ewm.baseService.util.page.CustomPageRequest;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findAllByPinned(Boolean pinned, CustomPageRequest pageable);
}