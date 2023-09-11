package ru.practicum.ewm.baseService.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String email;
    private Long id;
    private String name;
}