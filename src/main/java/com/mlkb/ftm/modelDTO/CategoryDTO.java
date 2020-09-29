package com.mlkb.ftm.modelDTO;

import com.mlkb.ftm.entity.GeneralType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private GeneralType type;
    private List<SubcategoryDTO> subcategories;
}
