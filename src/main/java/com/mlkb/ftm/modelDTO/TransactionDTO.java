package com.mlkb.ftm.modelDTO;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDTO {
    private Long id;
    private String title;
    private String type;
    private Double value;
    private Long categoryId;
    private Long subcategoryId;
    private Long payeeId;
    private Long accountId;
    private Date date;
}
