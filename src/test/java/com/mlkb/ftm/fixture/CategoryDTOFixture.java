package com.mlkb.ftm.fixture;

import com.mlkb.ftm.modelDTO.SubcategoryDTO;

public class CategoryDTOFixture {
    public static SubcategoryDTO carSubcategory() {
        var subcategoryDto = new SubcategoryDTO();
        subcategoryDto.setName("Car");

        return subcategoryDto;
    }
}
