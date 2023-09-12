package ru.practicum.baseService.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserRequest {
    @NotBlank
    private String name;
    @Email
    @NotNull
    @Size(min = 6, max = 254)
    private String email;
}