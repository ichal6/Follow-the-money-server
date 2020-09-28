package com.mlkb.ftm.modelDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewAccountDTO extends AccountDTO {
    private String userEmail;
}
