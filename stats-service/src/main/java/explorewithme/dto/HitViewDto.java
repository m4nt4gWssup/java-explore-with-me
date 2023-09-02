package explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HitViewDto {
    private String app;
    private String uri;
    private Long hits;
}
