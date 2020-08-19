package com.example.mlkb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToMany(targetEntity = Transaction.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Set<Transaction> transactions;
    @OneToMany
    @JoinColumn(name = "account_from_id")
    private Set<Transfer> transfersFrom;
    @OneToMany
    @JoinColumn(name = "account_to_id")
    private Set<Transfer> transfersTo;
}
