package com.pancaran.tutorial.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    private String username;

    private String password;

    private String name;

    private String token;

    private Long token_expire;

    @OneToMany(mappedBy = "user")
    private List<Contact> contacts;
}
