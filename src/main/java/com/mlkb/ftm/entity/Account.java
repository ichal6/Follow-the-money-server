package com.mlkb.ftm.entity;

import jakarta.validation.constraints.Digits;
import lombok.*;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedEntityGraph(
    name = "Account.transactions",
    attributeNodes = { @NamedAttributeNode("transactions") }
)
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
    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    private BigDecimal currentBalance;
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Currency currency;
    @Column(columnDefinition = "boolean default true")
    private Boolean isEnabled = true;
    @OneToMany(targetEntity = Transaction.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Set<Transaction> transactions;
    @OneToMany
    @JoinColumn(name = "account_from_id")
    private Set<Transfer> transfersFrom;
    @OneToMany
    @JoinColumn(name = "account_to_id")
    private Set<Transfer> transfersTo;

    public Double getCurrentBalance() {
        return this.currentBalance.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = BigDecimal.valueOf(currentBalance).setScale(2, RoundingMode.HALF_UP);
    }
}
