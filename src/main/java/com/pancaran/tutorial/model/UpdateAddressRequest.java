package com.pancaran.tutorial.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAddressRequest {
    @NotBlank
    @JsonIgnore
    private String contactId;

    @NotBlank
    @JsonIgnore
    private String addressId;

    @Size(min = 10)
    private String street;

    @Size(min = 5, max = 50)
    private String city;

    @Size(min = 5, max = 50)
    private String province;

    @NotBlank
    @Size(min = 5, max = 50)
    private String country;

    @NotNull
    private Integer zipCode;
}
