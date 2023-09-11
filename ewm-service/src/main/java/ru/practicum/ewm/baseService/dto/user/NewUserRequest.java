package ru.practicum.ewm.baseService.dto.user;

import lombok.*;
import ru.practicum.ewm.baseService.util.notblanknull.NotBlankNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserRequest {
    @NotBlank
    @Size(max = 128)
    private String name;
    @Email
    @NotBlankNull
    @Size(max = 255)
    private String email;
}