package com.example.mlkb.entity;

import javax.persistence.*;

@Entity
public class Payee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private GeneralType generalType;
}
