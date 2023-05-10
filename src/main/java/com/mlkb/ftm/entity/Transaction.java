package com.mlkb.ftm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private PaymentType type;
    private Double value;
    @CreatedDate
    private Date date;
    private String title;
    @OneToOne
    private Payee payee;
    @OneToOne
    private Category category;

    public Double getAbsoluteValue(){
        return Math.abs(value);
    }
}
