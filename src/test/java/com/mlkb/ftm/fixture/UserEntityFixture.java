package com.mlkb.ftm.fixture;

import com.mlkb.ftm.entity.User;

public class UserEntityFixture {
    public static User userUserowy() {
        User user = new User();
        user.setName("User Userowy");
        user.setEmail("user@user.pl");
        return user;
    }
}
