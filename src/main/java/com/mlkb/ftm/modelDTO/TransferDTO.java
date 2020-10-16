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
public class TransferDTO {
    private Long id;
    private String title;
    private Double value;
    private Long accountIdFrom;
    private Long accountIdTo;
    private Date date;
}
