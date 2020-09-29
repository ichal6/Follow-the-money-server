package com.mlkb.ftm.modelDTO;

import com.mlkb.ftm.entity.GeneralType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryDTO {
    private Long id;
    private String name;
    private GeneralType type;
}
