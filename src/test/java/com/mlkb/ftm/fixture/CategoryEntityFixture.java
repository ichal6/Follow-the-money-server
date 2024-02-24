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

    public static Category getTaxi() {
        Category category = new Category();
        category.setName("Taxi");
        category.setId(6L);
        category.setIsEnabled(true);
        category.setParentCategory(getTransport());

        return category;
    }
}
