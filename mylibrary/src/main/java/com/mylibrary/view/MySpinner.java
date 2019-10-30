package com.mylibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by zhixun on 2018/3/9 0009.
 */

@SuppressLint("AppCompatCustomView")
public class MySpinner extends Spinner {

    private int lastPosition = 0;

    public MySpinner(Context context, int mode) {
        super(context, mode);
    }

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    // 一个 item 选中的时候，总是会触发 setSelection 方法
// 所以在这个方法中，我们记录并检查上一次的selection position 就行了，如果是相同的，手动调用监听即可
    @Override
    public void setSelection(int position, boolean animate) {
        super.setSelection(position, animate);
        if (position == lastPosition) {
            getOnItemSelectedListener().onItemSelected(this, null, position, 0);
        }
        lastPosition = position;
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (position == lastPosition) {
            getOnItemSelectedListener().onItemSelected(this, null, position, 0);
        }
        lastPosition = position;
    }
}
