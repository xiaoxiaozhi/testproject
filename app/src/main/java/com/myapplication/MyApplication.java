package com.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

/**
 *
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getName(), "onCreate");
        Thread hand = new Thread(new Runnable() {
            @Override
            public void run() {
                //android.os.Process https://blog.csdn.net/Simon_Crystin/article/details/70315106  android 进城
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);//这个类属于android.os。设置线程优先级
            //https://blog.csdn.net/liujigangisgood/article/details/45165739 jdk设置优先级，java虚拟机设置优先级

            }
        });
        hand.setPriority(1);//java方法设置线程优先级，根据实际测试 android.os.Process设置优先级的办法有效
        hand.start();
        //一定要在主线程初始化的组件
        //.........................
        //如果有广告需要下载，在这里做。 这是一个封装好的service服务
        for (int i = 0; i < 10; i++) {
            Intent intent = new Intent(this, MyService.class);
            startService(intent);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //appllication 启动调用顺序attachBaseContext-》onCreate
        //计算程序启动初始时间 在activity的onWindowFocusChanged计算截面显示的截止时间
    }
}
