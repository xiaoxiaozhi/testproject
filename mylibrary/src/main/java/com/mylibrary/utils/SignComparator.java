package com.mylibrary.utils;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

/**
 * 返回标志字段转化为数字根据大小进行排序
 *
 * @param <T>
 */

public class SignComparator<T> implements Comparator<T> {
    private String method;

    public SignComparator(String method) {
        this.method = method;
    }

    @Override
    public int compare(T o1, T o2) {
        try {
            float data1 = Float.valueOf((String) o1.getClass().getMethod(method).invoke(o1));
            float data2 = Float.valueOf((String) o2.getClass().getMethod(method).invoke(o2));
            if (data1 > data2) {
                return 1;
            } else {
                return -1;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
