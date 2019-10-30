package com.mylibrary.utils;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class TimeComparator<T> implements Comparator<T> {
    private SimpleDateFormat sf;
    private String method;

    public TimeComparator(String method, String timeFormate) {
        sf = new SimpleDateFormat(timeFormate);
        this.method = method;
    }

    @Override
    public int compare(T o1, T o2) {
        try {
            Method m = o1.getClass().getMethod(method);
            Date date1 = sf.parse((String) m.invoke(o1));
            Date date2 = sf.parse((String) m.invoke(o2));
            Log.i("compare",date1+"\n"+date2);
            if (date1.before(date2)) {
                return 1; //放在队首
            } else if (date1.after(date2)) {
                return -1;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
