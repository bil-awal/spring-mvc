package com.pancaran.tutorial.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserRequest {

    @NotBlank
    @Size(min = 3, max = 30)
    private String username;

    @Size(min = 3, max = 99)
    private String password;
}
