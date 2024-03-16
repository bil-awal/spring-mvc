package com.pancaran.tutorial.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponse {
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;
}
