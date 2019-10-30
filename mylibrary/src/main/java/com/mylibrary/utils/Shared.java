package com.mylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by asus on 2016/10/18 0018.
 */

public class Shared {
    SharedPreferences mySharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public Shared(Context context, String name) {
        this.context = context;
        mySharedPreferences = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
        editor = mySharedPreferences.edit();
    }

    public void put(String key, String str) {
        editor.putString(key, str);
        editor.commit();
    }

    public void putInt(String key, int i) {
        editor.putInt(key, i);
        editor.commit();
    }

    public void putBoolean(String key, boolean b) {
        editor.putBoolean(key, b);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return mySharedPreferences.getBoolean(key, false);
    }

    public String get(String key) {
        return mySharedPreferences.getString(key, "");
    }

    public int getInt(String key) {
        return mySharedPreferences.getInt(key, -1);
    }
}
