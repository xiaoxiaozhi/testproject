package com.mylibrary.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MySort<T> {
    private Comparator<T> signComparator;
    private Comparator<T> timeComparator;
    private String signMethod, timeMethod;

    public MySort setTimeComparator(String timeMethod, Comparator<T> tc) {
        this.timeComparator = tc;
        this.timeMethod = timeMethod;
        return this;
    }

    public MySort setSignComparator(String signMethod, Comparator<T> sc) {
        this.signComparator = sc;
        this.signMethod = signMethod;
        return this;
    }

    public MySort setTop() {
        return this;
    }

    public void sort(List<T> lists) {
        Collections.sort(lists, signComparator);
        int cousor = 0;

        for (int i = 0; i < lists.size(); i++) {
            T t = lists.get(i);
            if (i + 1 == lists.size()) {
                break;
            }
            T tnext = lists.get(i + 1);
            try {
                float sign = Float.valueOf((String) t.getClass().getMethod(signMethod).invoke(t));
                float signext = Float.valueOf((String) t.getClass().getMethod(signMethod).invoke(tnext));
                if (sign != signext) {

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
