package com.myapplication.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.myapplication.R

class kotlinActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        //类型后面加?表示可为空
        var age: String? = "123"
        //如果是空抛出空指针异常
        val ages = age!!.toInt()
        //如果空返回 null
        val ages1 = age?.toInt()
        for (i in 4..1 step 2) print(i) // 输出“42”
    }
}
