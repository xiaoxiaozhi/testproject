package com.myapplication.activity

import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.LinearLayout
import com.myapplication.interfac.OperationInterface

import com.myapplication.R
import kotlinx.android.synthetic.main.activity_base.*

/**
 * 透明栏和沉浸式：前者 状态栏和导航栏都是透明，后者隐藏
 * 透明栏：布局会放在状态栏和导航栏下面
 * 参考https://www.jianshu.com/p/140be70b84cd?utm_source=tuicool&utm_medium=referral
 */
open class BaseActivity : AppCompatActivity(), OperationInterface {
    private var isTanslucentStatus: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //代码设置透明状态栏
            // 当然也可以在theme的样式文件里添加style:<item name="android:windowTranslucentStatus">true</item>,
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明导航栏
        }
        initToolbar(topbar)
    }

    override fun setContentView(layoutResID: Int) {
        if (layoutResID == R.layout.activity_base) {
            super.setContentView(layoutResID)
            if (statusVisible()) {
                baseLayout.fitsSystemWindows = true
            }
        }
        findViewById<LinearLayout>(R.id.container).addView(layoutInflater.inflate(layoutResID, null))
    }

    fun setTitle(title: String) {
        topbar.title = title//直接引用ID。静态布局引入
    }

    private fun getStatusBarHeight(context: Context): Int {
        var height = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(height);
    }

}
