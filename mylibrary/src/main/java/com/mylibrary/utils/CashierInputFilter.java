package com.mylibrary.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhixun on 2017/10/26 0026.
 */

public class CashierInputFilter implements InputFilter {
    Pattern mPattern;

    //输入的最大金额
    private static final int MAX_VALUE = Integer.MAX_VALUE;
    //小数点后的位数
    private static final int POINTER_LENGTH = 2;

    private static final String POINTER = ".";

    private static final String ZERO = "0";
    private String max;

    public CashierInputFilter(String maxNum) {
        mPattern = Pattern.compile("([0-9]|\\.)*");
        max = maxNum;
    }

    /**
     * @param source 新输入的字符串
     * @param start  新输入的字符串起始下标，一般为0
     * @param end    新输入的字符串终点下标，一般为source长度-1
     * @param dest   输入之前文本框内容
     * @param dstart 原内容起始坐标，一般为0
     * @param dend   原内容终点坐标，一般为dest长度-1
     * @return 输入内容
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (Integer.valueOf(dest.toString() + String.valueOf(source)) > Integer.valueOf(max)) {
            return max;
        } else {
            return dest.toString() + String.valueOf(source);
        }

    }
}
