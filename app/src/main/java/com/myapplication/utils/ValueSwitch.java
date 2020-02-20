package com.myapplication.utils;

import android.content.Context;
import android.util.TypedValue;


/**
 * 用TypeValue转换单位
 */
public class ValueSwitch {
    public ValueSwitch(Context context) {
        // 25sp 转换成 px。举一反三通过更换unit 可以把dp、mm、pt转换成px
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 25, context.getResources().getDisplayMetrics());
    }

}
