package com.myapplication.module;

import java.util.Random;

public class UserHandler {
    private User user;

    public UserHandler(User user) {
        this.user = user;
    }

    public void changeName() {
        user.setName("改变名字" + new Random().nextInt(100));
    }

    public void changePwd() {
        user.setPassword("改变密码" + new Random().nextInt(100));
        user.setName("改变名字" + new Random().nextInt(100));
    }
}
