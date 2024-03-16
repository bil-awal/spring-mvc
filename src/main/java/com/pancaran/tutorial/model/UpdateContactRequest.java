package com.pancaran.tutorial.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateContactRequest {
    @NotBlank
    @JsonIgnore
    private String id;

    @NotBlank
    @Size(min = 3, max = 20)
    private String firstName;

    @Size(min = 3, max = 20)
    private String lastName;

    @Size(min = 3, max = 100)
    @Email
    private String email;

    @Pattern(regexp = "^[+]?[0-9\\s()-]+$", message = "Invalid phone number")
    private String phone;
}
