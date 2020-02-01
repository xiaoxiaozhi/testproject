package com.myapplication

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.myapplication.activity.BaseActivity

/**
 * 一、启动优化：1.视觉优化，先用一个启动activity作为过渡，（感觉慢了一点存疑，要验证一下）
 * 2.页面启动时间统计 adb shell am start -S -R 10 -W 包名/activity的全限定名 -S表示重启当前应用 只启动这个activity，
 * -R 10 代表重启次数，取平均值就是该activity的启动时间
 * 前面的activity没动ThisTime activity的启动时间；TotalTime 据说是application启动时间+thisTime根绝观察和thisTime一样存疑；
 * WaitTime 进城创建+thisTime；优化思路 系统创建进城的时间优化不了，只能从thisTime入手
 * 3.application 优化（看MyApplication类在里面做好记录），把buglly 地图类sdk 友盟 极光 的sdk放在子线程中初始化，并且将子线程优先级调低android.os.Prosecc
 * 4.固定一个闪屏页，时间=组建初始化时间+闪屏页展示时间 给。具体解释在MyApplication
 * 5.首页广告加载用intentServer下载
 * 二、帧优化：https://www.jianshu.com/p/b98d0dc5362c
 * 60fps：在与手机交互过程中，如触摸和反馈60帧以下人是能够感觉出来的，60帧以上不能察觉变化
 * 为了能够实现60fps，这意味着计算渲染的大多数操作都必须在16ms内完成。
 */
class LaunchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_launch)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)//代码设置全屏，本例用主题
//        window.decorView.setBackground(resources.getDrawable(R.mipmap.ic_launcher, null))//代码设置窗口背景图，本例在主题上设置
        startActivity(Intent(this, MainActivity::class.java))
        Log.i(this.javaClass.name,"onCreate");
        finish()//MainActivity的绘制和finish结束几乎同一时间
    }
}
