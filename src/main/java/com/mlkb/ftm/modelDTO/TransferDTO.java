package com.mlkb.ftm.modelDTO;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransferDTO {
    private Long id;
    private String title;
    private Double value;
    private Long accountIdFrom;
    private Long accountIdTo;
    private Date date;
}
