package com.example.mlkb.entity;

import javax.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private AccountType accountType;
    @Column(columnDefinition = "double precision default 0.00")
    private Double startingBalance;
    @Column(columnDefinition = "double precision default 0.00")
    private Double currentBalance;
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Currency currency;

}
