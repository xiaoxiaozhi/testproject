package com.myapplication.activity

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.myapplication.interfac.OperationInterface

import com.myapplication.R
import com.myapplication.utils.Density
import kotlinx.android.synthetic.main.activity_base.*

/**
 * 透明栏和沉浸式：前者 状态栏和导航栏都是透明，后者隐藏
 * 透明栏：布局会放在状态栏和导航栏下面
 * 参考https://www.jianshu.com/p/140be70b84cd?utm_source=tuicool&utm_medium=referral
 */
open class BaseActivity : AppCompatActivity(), OperationInterface {
    private var isTanslucentStatus: Boolean = false;
    //    private static

    companion object {
        @JvmField
        var isDensity: Boolean = true;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isDensity) {
            modifyDensity(this, application)
            isDensity = false
        }
        if (fullScreen()) {
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                var v = this.getWindow().getDecorView();
                v.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                //for new api versions.
                var decorView = getWindow().getDecorView()
                var uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }


        }
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
        container.addView(layoutInflater.inflate(layoutResID, null))
    }

    fun setTitle(title: String) {
        topbar.title = title//直接引用ID。静态布局引入
    }

    /**
     * 获取状态栏高度
     */
    public fun getStatusBarHeight(): Int {
        var height = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(height);
    }

    /**
     * 获取导航栏高度
     */
    public fun getNavigationBarHeight(): Int {
        val resources = getResources()
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        val height = resources.getDimensionPixelSize(resourceId)
        Log.v("navigation bar>>>", "height:$height")
        return height

    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.i(this.javaClass.name, "内存状态标志$level----------")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.i(this.javaClass.name, "onLowMemory这个方法回调意味着后台进程被干掉")
    }

    private fun modifyDensity(activity: Activity, application: Application) {
        //今日头条适配方案 https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
        //dpi 斜对角像素密度：勾股定律 根号((wpx * wpx)+(hpx*hpx))/inc（英寸本机是5）
        //density = dpi/160
        //dp = px/density

        var sourceDensity = activity.resources.displayMetrics.density
        var targetDensity: Float = activity.resources.displayMetrics.widthPixels.toFloat() / 720
        var targetScaleDensity: Float = activity.resources.displayMetrics.density / (sourceDensity / activity.resources.displayMetrics.scaledDensity)
        var targetDpi: Int = (activity.resources.displayMetrics.density * 160).toInt()
        var activityDisplayMetrics: DisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaleDensity
        activityDisplayMetrics.densityDpi = targetDpi

        var applicationDisplayMetrics = application.resources.displayMetrics
        applicationDisplayMetrics.density = targetDensity
        applicationDisplayMetrics.scaledDensity = targetScaleDensity
        applicationDisplayMetrics.densityDpi = targetDpi

//        application.registerComponentCallbacks(object : ComponentCallbacks {
//            override fun onLowMemory() {
//            }
//
//            override fun onConfigurationChanged(newConfig: Configuration?) {
//                if(){
//
//                }
//            }
//        })
//        val displayMetrics = resources.displayMetrics
//        val cfg = resources.configuration
//        displayMetrics.density = displayMetrics.widthPixels.toFloat() / 720
//        displayMetrics.scaledDensity = displayMetrics.density / (sourceDensity / displayMetrics.scaledDensity)
//        displayMetrics.densityDpi = (displayMetrics.density * 160).toInt();//只修改density 还不行dpi也要修改
//        cfg.densityDpi = displayMetrics.densityDpi;// 必要
//        cfg.fontScale = displayMetrics.scaledDensity //必要
        Log.i("修改density", "density:" + activity.resources.displayMetrics.density + " densityDpi:" + activity.resources.displayMetrics.densityDpi + " scaledDensity:" + activity.resources.displayMetrics.scaledDensity)
//        resources.updateConfiguration(cfg, displayMetrics)
    }
}
