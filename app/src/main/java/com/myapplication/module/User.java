package com.myapplication.module;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.myapplication.BR;

import java.util.Random;

public class User extends BaseObservable {
    public User(String name, String pwd) {
        setName(name);
        setPassword(pwd);
    }

    @Bindable
    private String name;
    @Bindable
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        //只更新本字段,字段变量必须加@Bindable
        notifyPropertyChanged(BR.name);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        //全部更新，变量不用加@Bindable
        notifyChange();
    }
}
