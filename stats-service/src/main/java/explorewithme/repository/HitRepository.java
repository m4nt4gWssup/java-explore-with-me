package explorewithme.repository;

import explorewithme.dto.HitViewDto;
import explorewithme.model.Hit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query(" SELECT new explorewithme.dto.HitViewDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND (h.uri IN (?3) OR (?3) is NULL) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC ")
    List<HitViewDto> findUniqueViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, PageRequest pageable);


    @Query(" SELECT new explorewithme.dto.HitViewDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND (h.uri IN (?3) OR (?3) is NULL) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC ")
    List<HitViewDto> findViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, PageRequest pageable);
}
