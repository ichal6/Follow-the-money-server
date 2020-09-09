package com.mlkb.ftm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
