package com.mlkb.ftm.modelDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private Long id;
    private String title;
    private String payeeFrom;
    private String payeeTo;
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date date;
    private Double cost;
}
