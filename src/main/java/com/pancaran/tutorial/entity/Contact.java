package com.pancaran.tutorial.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String phone;

    private String email;

    @OneToMany(mappedBy = "contact")
    private List<Address> addresses;
}
