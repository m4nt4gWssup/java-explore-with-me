package explorewithme.repository;

import explorewithme.dto.HitViewDto;
import explorewithme.model.Hit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query(" SELECT new explorewithme.dto.HitViewDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (h.uri IN (:uris) OR :uris is NULL) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC ")
    List<HitViewDto> findUniqueViewStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                         @Param("uris") List<String> uris, PageRequest pageable);

    @Query(" SELECT new explorewithme.dto.HitViewDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (h.uri IN (:uris) OR :uris is NULL) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC ")
    List<HitViewDto> findViewStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris, PageRequest pageable);
}
