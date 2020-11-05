package com.myapplication.module;


import androidx.databinding.ObservableField;
import androidx.databinding.ObservableFloat;

public class ObservableUser {

    public ObservableField<String> name;

    public ObservableFloat price;

    public ObservableField<String> details;

    public ObservableUser(String name, float price, String details) {
        this.name = new ObservableField<>(name);
        this.price = new ObservableFloat(price);
        this.details = new ObservableField<>(details);
    }
}
