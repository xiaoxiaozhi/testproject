package com.myapplication.activity


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.myapplication.R

class HandlerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler)
        //对于Looper主要是prepare()和loop()两个方法。
        Thread(object : Runnable {
            override fun run() {
//                Thread<->Looper<->msgQunen
                //Looper对象的作用：保证一个线程对应一个MessageQueue、Looper。然后loop不断循环发送消息
                Looper.prepare()//创建Looper和MessageQueue(创建好的Looper放入ThreadLocal和线程一一对应)
                Looper.myLooper()//从ThreadLocal中取出Looper。这样的话prepare和myLooper都必须在线程中执行才有用
                val handler = object : Handler() {
                    // 取出当前线程的looper，
                    override fun handleMessage(msg: Message?) {
                        Log.i(javaClass.name, msg!!.what.toString())
                        super.handleMessage(msg)
                    }
                }
                handler.sendEmptyMessage(222)//qunen中放入msg，接下来开始循环
                Looper.loop()//取出MessageQueue 开始无限循环当msg==null结束循环msg!=null分发给Handler处理（threadlocal->looper->MessageQueue）

                handler.post(object : Runnable {
                    // 这个方法其实并没有创建线程而是发送了一条信息 sendMessageDelayed
                    override fun run() {
                    }
                })
            }
        }).start()
    }
//    Android的Handler机制详解3_Looper.looper()不会卡死主线程
//    参考https://blog.csdn.net/qian520ao/article/details/78262289 包含handle源码分析，在文章中给了一个链接
//    private fun prepare(quitAllowed: Boolean) {
//        if (sThreadLocal.get() != null) {
//            throw RuntimeException("Only one Looper may be created per thread")
//        }
//        sThreadLocal.set(Looper(true))
//    }
}
