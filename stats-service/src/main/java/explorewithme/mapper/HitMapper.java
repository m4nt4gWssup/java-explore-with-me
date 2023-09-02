package explorewithme.mapper;

import explorewithme.dto.HitDto;
import explorewithme.model.Hit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HitMapper {
    public HitDto toDto(Hit hit) {
        return new HitDto(hit.getId(), hit.getApp(), hit.getUri(), hit.getIp(),
                hit.getTimestamp());
    }
}
