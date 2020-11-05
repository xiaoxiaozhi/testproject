package com.myapplication.activity


import android.os.Bundle
import com.myapplication.R
import android.app.Activity
import com.myapplication.MainActivity


/**
 * 测试内存泄漏:android profiler  进入内存页面，在分析前点击左上角垃圾回收站标志清理干净。然后点击dump java heap开始分析
 * 如果界面上能看到刚才点击的activity则认为有内存泄漏，当然实际情况点击过的activity都显示，那么只要Shallow Size和Retained Size分配的相等且够小也认为回收干净
 *
 */
class MemeryActivity : BaseActivity() {
    private var sActivity: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_memery)
//        sActivity = this
//        finish.setOnClickListener { finish() }
        MainActivity.ma = this
    }
}
