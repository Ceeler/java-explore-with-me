package ru.practicum.statmain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

    @Email
    @NotBlank
    @Size(min = 6, max = 254)
    private String email;
}
