package com.mlkb.ftm.modelDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Boolean isInternal;
    private Long id;
    private Double value;
    private Date date;
    private String title;
    private String from;
    private String to;
    private String categoryName;
    private Double balanceAfter;
}
