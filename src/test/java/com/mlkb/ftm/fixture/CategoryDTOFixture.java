package com.mlkb.ftm.fixture;

import com.mlkb.ftm.entity.GeneralType;
import com.mlkb.ftm.modelDTO.SubcategoryDTO;

public class CategoryDTOFixture {
    public static SubcategoryDTO carSubcategory() {
        var subcategoryDto = new SubcategoryDTO();
        subcategoryDto.setName("Car");
        subcategoryDto.setType(GeneralType.EXPENSE);

        return subcategoryDto;
    }
}
