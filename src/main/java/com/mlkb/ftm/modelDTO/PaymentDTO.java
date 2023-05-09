package com.mlkb.ftm.modelDTO;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentDTO {
    private Boolean isInternal;
    private Long id;
    private Double value;
    private Date date;
    private String title;
    private String from;
    private String to;
    private String categoryName;
    private BigDecimal balanceAfter;
}
