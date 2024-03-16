package com.pancaran.tutorial.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    private Contact contact;

    private String street;

    private String city;

    private String country;

    private String province;

    @Column(name = "zip_code")
    private Integer zipCode;
}
