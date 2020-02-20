package com.myapplication.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import com.myapplication.R
import kotlinx.android.synthetic.main.activity_constraint_layout_test.*

/**
 * 1. android:clipChildren 子控件按照自己需求大小绘制，
 * 不再受父控件约束。需要在祖父设置，这样的话，（不管几层嵌套在根布局设置，存疑）
 * 2.半径 layout_constraintCircleRadius 角度layout_constraintCircleAngle .角度从y轴正半轴开始
 * 3.margin只能大于等于0 goneMargin主要用于约束的控件可见性被设置为gone的时候使用的margin值。
 * 例如layout_goneMarginLeft=10dp 例如左边的控件消失 该控件就会距离左边10dp 实际测试普通的marginLeft效果一样
 * android:layout_marginStart  android:layout_marginEnd:从左到右的布局
 */
class ConstraintLayoutTestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout_test)
        test5.setOnClickListener(View.OnClickListener { test5.visibility = View.GONE })
    }

    override fun initToolbar(toolbar: Toolbar) {
        toolbar.setTitle("ConstraintLayout布局学习")
        toolbar.navigationIcon = resources.getDrawable(R.drawable.back, null);
    }
}
