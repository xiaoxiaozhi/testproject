package com.myapplication

import android.app.PendingIntent.getActivity
import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.myapplication.activity.BaseActivity

/**
 * 一、启动优化： 参考https://blog.csdn.net/qian520ao/article/details/81908505
 * 1.视觉优化，先用一个启动activity作为过渡，
 * 2.页面启动时间统计 adb shell am start -S -R 10 -W 包名/activity的全限定名 -S表示重启当前应用 只启动这个activity，
 * -R 10 代表重启次数，取平均值就是该activity的启动时间
 * 前面的activity没动ThisTime activity的启动时间；TotalTime 据说是application启动时间+thisTime根绝观察和thisTime一样存疑；
 * WaitTime 进城创建+thisTime；优化思路 系统创建进城的时间优化不了，只能从thisTime入手
 * 3.application 优化（看MyApplication类在里面做好记录），把buglly 地图类sdk 友盟 极光 的sdk放在子线程中初始化，并且将子线程优先级调低android.os.Prosecc
 * 4.固定一个闪屏页，时间=组建初始化时间+闪屏页展示时间 给。具体解释在MyApplication
 * 5.首页广告加载用intentServer下载
 * */
class LaunchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_launch)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)//代码设置全屏，本例用主题
//        window.decorView.setBackground(resources.getDrawable(R.mipmap.ic_launcher, null))//代码设置窗口背景图，本例在主题上设置
        startActivity(Intent(this, MainActivity::class.java))
        Log.i(this.javaClass.name, "onCreate");
        finish()//MainActivity的绘制和finish结束几乎同一时间
    }
    /**
     *内存优化：https://www.jianshu.com/p/1972a6d1f0fc 内存分析
     *AndroidStudio  profile 使用 https://blog.csdn.net/Double2hao/article/details/78784758
     *内存泄漏：循环中创建大量临时对象、onDraw中创建Paint或Bitmap对象
     *内存溢出：SparseArray、ArrayMap用来代替HashMap、优化bitmap显示
     *内存泄漏导致，内存溢出
     */
}
