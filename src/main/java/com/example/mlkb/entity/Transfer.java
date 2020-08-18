package com.example.mlkb.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.util.Date;

@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @DecimalMin(value = "0.0")
    private Double value;
    private String title;
    @CreatedDate
    private Date date;

    @OneToOne
    private Account accountFrom;
    @OneToOne
    private Account accountTo;

}
