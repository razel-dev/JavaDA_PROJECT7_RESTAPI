package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table( name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "password")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "username", nullable = false, length = 125)
    private String username;

    @Column(name = "password", nullable = false, length = 125)
    private String password;

    @Column(name = "fullname", nullable = false, length = 125)
    private String fullname;

    @Column(name = "role", nullable = false, length = 125)
    private String role;


    }
