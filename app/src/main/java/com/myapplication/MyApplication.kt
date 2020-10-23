package com.myapplication

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Log
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS


/**
 * 1.启动优化
 * 2.进程保活：从6.0开始一段时间没有操作会杀死所有进程。AndroidManifest.xml 里面配置<uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
 * 实现保活 参考https://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650829883&idx=1&sn=d6349cc41d5bd948d900989d9b6b1f41&chksm=80b7a0a5b7c029b336ed0496af61209bbef1a1ffeebda870fb0f843efa695cb7b6db35af537c&scene=126&sessionid=1581831182&key=5f715980a7f4cae029e529cee224df9c3a710a35bd0e75317f96422f464feea9047d1f574223245d950f828f24a551eae8bb35026be1bdc8fe92972d7035a39da6df1a4b0235d75143575b285657baaa&ascene=1&uin=OTU5NTc0NDYw&devicetype=Windows+10&version=6208006f&lang=zh_CN&exportkey=ATixr9Cf3cNM%2Bt22BaQxXuY%3D&pass_ticket=B7VH2HjiFmqHo9bccUE2K87D5mHxUsf%2FXQiHdu4MAgaAxlHZNELuGlCYw8MOSjrP
 * TODO www.52im.net/thread-2893-1-1.html
 * TODO http://www.52im.net/thread-2921-1-1.html
 * 3.线程优化：（1）nice value 和 cgroup。前者的值越小线程的优先级越低。工作量越大的线程优先级要设置的越小。这样引出来一个问题如果app的线程有20个呢尽管优先级很低也会影响ui的执行
 * 此时此刻，android使用cgroup分组把低优先级的线程分一组即时很多他们的执行机会也只有5%到10%。不会影响ui'线程的执行
 * （2）不要在非ui线程中引用ui对象防止内存泄漏
 * （3）android 提供的五种线程方式
 *     ①Thread 匿名内部类默认持有对外部类的引用容易造成内存泄漏、创建和回收线程需要消耗资源、不易管理
 *     ②AsyncTask 串行执行，前一个执行完成后才能执行后一个容易阻塞
 *     ③HandlerThread 串行执行，不能并发带来，优先级高同ui线程相同，既是优点也是缺点本质上是thread+looper，能够长期存在的线程，主线程中的handle接收到它的looper，主线程调用sendMessage方法。回调handleMessage方法工作在工作线程中
 *     ④IntentService ntentService继承自普通Service同时又在内部创建了一个HandlerThread，所以有handlerThread的优缺点，执行完成后服务自动关闭
 *     ⑤ThreadPoolExceutor
 *     ①② 适合适合单个任务场景  ③适合串行多任务场景 ④适合与ui无关的多任务 ⑤最适合并行多任务场景
 */
class MyApplication : Application() {
    companion object {
        private var instance: Application? = null;
        fun getInstance() = instance!!;
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(this.javaClass.name, "onCreate")
        instance = this;
        val hand = Thread(Runnable {
            //android.os.Process https://blog.csdn.net/Simon_Crystin/article/details/70315106  android 进城
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)//这个类属于android.os。线程划入低优先级组
            //https://blog.csdn.net/liujigangisgood/article/details/45165739 jdk设置优先级，java虚拟机设置优先级
        })
        hand.priority = 1//java方法设置线程优先级，根据实际测试 android.os.Process设置优先级的办法有效
        hand.start()
        //一定要在主线程初始化的组件
        //.........................
        //如果有广告需要下载，在这里做。 这是一个封装好的异步service服务
        for (i in 0..9) {
            val intent = Intent(this, MyService::class.java)
            startService(intent)
        }

        //HandlerThread 示例
        val mHandlerThread = HandlerThread("handlerThread");
// 步骤2：启动线程
        mHandlerThread.start();
        val workHandler = object : Handler(mHandlerThread.looper) {
            override fun handleMessage(msg: Message?) {
                Log.i(javaClass.simpleName, "线程：" + Thread.currentThread().name + " 接收到的消息:" + msg!!.what.toString())
            }
        }
        workHandler.sendEmptyMessage(111)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //appllication 启动调用顺序attachBaseContext-》onCreate
        //计算程序启动初始时间 在activity的onWindowFocusChanged计算截面显示的截止时间
    }


    /**
     * onTrimMemory的参数是一个int数值，代表不同的内存状态：
     *
     *
     * TRIM_MEMORY_RUNNING_MODERATE：5
     *
     *
     * 你的应用正在运行并且不会被列为可杀死的。但是设备此时正运行于低内存状态下，系统开始触发杀死LRU Cache中的Process的机制。
     *
     *
     * TRIM_MEMORY_RUNNING_LOW：10
     *
     *
     * 你的应用正在运行且没有被列为可杀死的。但是设备正运行于更低内存的状态下，你应该释放不用的资源用来提升系统性能。
     *
     *
     * TRIM_MEMORY_RUNNING_CRITICAL：15
     *
     *
     * 你的应用仍在运行，但是系统已经把LRU Cache中的大多数进程都已经杀死，因此你应该立即释放所有非必须的资源。如果系统不能回收到足够的RAM数量，系统将会清除所有的LRU缓存中的进程，并且开始杀死那些之前被认为不应该杀死的进程，例如那个包含了一个运行态Service的进程。
     *
     *
     * 当应用进程退到后台正在被Cached的时候，可能会接收到从onTrimMemory()中返回的下面的值之一：
     *
     *
     * TRIM_MEMORY_BACKGROUND：40
     *
     *
     * 系统正运行于低内存状态并且你的进程正处于LRU缓存名单中最不容易杀掉的位置。尽管你的应用进程并不是处于被杀掉的高危险状态，系统可能已经开始杀掉LRU缓存中的其他进程了。你应该释放那些容易恢复的资源，以便于你的进程可以保留下来，这样当用户回退到你的应用的时候才能够迅速恢复。
     *
     *
     * TRIM_MEMORY_MODERATE：60
     *
     *
     * 系统正运行于低内存状态并且你的进程已经已经接近LRU名单的中部位置。如果系统开始变得更加内存紧张，你的进程是有可能被杀死的。
     *
     *
     * TRIM_MEMORY_COMPLETE：80
     *
     *
     * 系统正运行于低内存的状态并且你的进程正处于LRU名单中最容易被杀掉的位置。你应该释放任何不影响你的应用恢复状态的资源。
     *
     *
     * TRIM_MEMORY_UI_HIDDEN：20
     *
     *
     * UI不可见了，应该释放占用大量内存的UI数据。
     *
     *
     * 比如说一个Bitmap，我们缓存下来是为了可能的(不一定)再次显示。但是如果接到这个回调，那么还是将它释放掉，如果回到前台，再显示会比较好。
     *
     * @param level
     */
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        val resultStr = when (level) {
            20 -> "UI不可见了，应该释放占用大量内存的UI数据"
            80 -> "系统正运行于低内存的状态并且你的进程正处于LRU名单中最容易被杀掉的位置。你应该释放任何不影响你的应用恢复状态的资源"
            60 -> "系统正运行于低内存状态并且你的进程已经已经接近LRU名单的中部位置。如果系统开始变得更加内存紧张，你的进程是有可能被杀死的。"
            40 -> "系统正运行于低内存状态并且你的进程正处于LRU缓存名单中最不容易杀掉的位置。尽管你的应用进程并不是处于被杀掉的高危险状态，系统可能已经开始杀掉LRU缓存中的其他进程了。你应该释放那些容易恢复的资源，以便于你的进程可以保留下来，这样当用户回退到你的应用的时候才能够迅速恢复。"
            15 -> "你的应用仍在运行，但是系统已经把LRU Cache中的大多数进程都已经杀死，因此你应该立即释放所有非必须的资源。如果系统不能回收到足够的RAM数量，系统将会清除所有的LRU缓存中的进程，并且开始杀死那些之前被认为不应该杀死的进程，例如那个包含了一个运行态Service的进程。"
            10 -> "你的应用正在运行且没有被列为可杀死的。但是设备正运行于更低内存的状态下，你应该释放不用的资源用来提升系统性能。"
            5 -> "你的应用正在运行并且不会被列为可杀死的。但是设备此时正运行于低内存状态下，系统开始触发杀死LRU Cache中的Process的机制"
            else -> {
                "未发现状态"
            }
        }
        Log.i(this.javaClass.name, "$level-------$resultStr")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.i(this.javaClass.name, "这个方法的回调意味着后台进程被干掉了")
    }


}
