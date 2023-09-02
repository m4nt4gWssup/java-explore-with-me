package explorewithme.mapper;

import explorewithme.dto.HitViewDto;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class HitViewMapper {

    public HitViewDto toDto(HitViewDto hitDto) {
        return new HitViewDto(hitDto.getApp(), hitDto.getUri(), hitDto.getHits());
    }

    public List<HitViewDto> toDtoList(List<HitViewDto> viewHits) {
        return viewHits.stream().map(HitViewMapper::toDto).collect(Collectors.toList());
    }
}
