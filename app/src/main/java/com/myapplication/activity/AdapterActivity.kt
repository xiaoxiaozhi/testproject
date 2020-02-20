package com.myapplication.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager

import com.myapplication.R
import com.mylibrary.utils.StatusBarUtil.getStatusBarHeight
import kotlinx.android.synthetic.main.activity_adapter.*


/**
 * 屏幕适配
 * 参考 https://www.jianshu.com/p/cd66b7e01d4a
 * 参考 https://blog.csdn.net/wawxf2008/article/details/47379577
 * 屏幕适配原理描述：dp = px/density 例如一720*1280dp 为基准。
 * mate8 宽度1080px 那么720=1080/density  density=1.5
 * 既可实现适配。下面是测试代码
 */
class AdapterActivity : BaseActivity() {
    override fun initToolbar(toolbar: Toolbar) {
        toolbar.setTitle("屏幕适配")
        toolbar.setNavigationIcon(R.drawable.back)
    }

    override fun fullScreen(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //修改屏幕density 具体做法放在application里面作为
        setContentView(R.layout.activity_adapter)
        getDisplayInfo()
        t.postDelayed(Runnable { Log.i("控件宽高", t.height.toString() + "/" + t.width) }, 1000)
    }

    /**
     * 获取屏幕信息 https://www.cnblogs.com/feng-ye/p/5923019.html
     */
    internal fun getDisplayInfo() {
        //1.从windowmanager中获取屏幕信息
        val wm = getSystemService(
                Context.WINDOW_SERVICE) as WindowManager
        val metric = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metric)
        //2.从display处获取屏幕信息
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        //3.resource获取屏幕信息
        val displayMetrics = resources.displayMetrics
        // 获取屏幕宽高的时候，高度不包含虚拟导航栏高度。本机实际尺寸5英寸
        //dpi :每英寸像素  根据勾股定 斜对角像素数/英寸
        Log.i("屏幕信息", "状态栏高度：" + getStatusBarHeight() + " 导航栏高度: " + getNavigationBarHeight())
        Log.i("屏幕信息", "宽：" + displayMetrics.widthPixels + " 高: " + displayMetrics.heightPixels)
        Log.i("屏幕信息", "density：" + displayMetrics.density)//像素密度比值 density = dpi / 160
        Log.i("屏幕信息", "densityDpi：" + displayMetrics.densityDpi)//densityDpi就是dpi
        Log.i("屏幕信息", "文字：" + displayMetrics.scaledDensity)
        //因为是一块屏幕垂直方向和水平方向像素密度基本一致，那么斜对角像素密度为什么不一致？
        Log.i("屏幕信息", "水平方向像素密度xdpi：" + displayMetrics.xdpi + " 垂直方向像素密度ydpi:" + displayMetrics.ydpi)
        Log.i("屏幕信息", "自己计算英寸:" + Math.sqrt(Math.pow(displayMetrics.widthPixels.toDouble(), 2.0) + Math.pow((displayMetrics.heightPixels + getNavigationBarHeight()).toDouble(), 2.0)) /
                displayMetrics.densityDpi)
        Log.i("屏幕信息", "自己计算densityDpi:" + Math.sqrt(Math.pow(displayMetrics.widthPixels.toDouble(), 2.0) + Math.pow((displayMetrics.heightPixels + getNavigationBarHeight()).toDouble(), 2.0)) / 5)
        Log.i("屏幕信息", "自己计算density:" + (Math.sqrt(Math.pow(displayMetrics.widthPixels.toDouble(), 2.0) + Math.pow((displayMetrics.heightPixels + getNavigationBarHeight()).toDouble(), 2.0)) / 5) / 160)
    }

}
