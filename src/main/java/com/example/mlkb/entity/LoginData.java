package com.example.mlkb.entity;

import javax.persistence.*;

@Entity
public class LoginData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String email;
    private Long hashPassword;
}
