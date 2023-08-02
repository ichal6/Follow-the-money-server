package com.mlkb.ftm.fixture;

import com.mlkb.ftm.entity.Category;

public class CategoryEntityFixture {
    public static Category getTransport() {
        Category category = new Category();
        category.setName("Transport");
        category.setId(5L);
        category.setIsEnabled(true);

        return category;
    }
}
