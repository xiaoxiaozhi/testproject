package com.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * 实现Checkable 接口，就能想radioButton一样实现选中状态
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable, View.OnClickListener {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    private boolean mChecked = false;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean b) {
        if (b != mChecked) {
            mChecked = b;
            refreshDrawableState();
        }
    }

    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void onClick(View v) {
        Log.i(getClass().getSimpleName(), "点击");
        ;
        if (isChecked()) {
            setChecked(false);
        } else {
            setChecked(true);
        }
    }
}