package explorewithme.service;

import explorewithme.dto.HitDto;
import explorewithme.dto.HitViewDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    HitDto create(HitDto hitDto);

    List<HitViewDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
