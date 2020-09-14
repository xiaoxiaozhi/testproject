package com.myapplication.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.myapplication.R
import com.myapplication.module.KotlinClass

/**
 * 吐槽kotlin语法难看的帖子，下面是反对评论，各抒己说的都很非常精彩https://mp.weixin.qq.com/s?src=11&timestamp=1583758428&ver=2206&signature=MEidSwlUwjiOXNmlLrq*2198xwhAwGJ1WCzp9MHUExhdfc5q6v3k9ZAtR1Ieh0weABaR0skETgSszWJrnFJ7Vup
 * -IZ9P
 * -bDzHcD4V9e9fRQaswR7s*am0jPgunmmBbRv&new=1
 *
 */
class kotlinActivity : BaseActivity() {
    //静态代码块
    companion object {
        init {
            //你想静态化的东西,外面不要有函数
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        //1.基础语法
        //类型后面加?表示可为空
        var age: String? = "123"
        //如果是空抛出空指针异常
        val ages = age!!.toInt()
        //如果空返回 null
        val ages1 = age?.toInt()
        for (i in 4..1 step 2) print(i) // 输出“42”
        //1.1 when 表达式，代替了switch when既可以当表达式使用也可以作为语句，when作为表达式的时候必须有else分支
        val s = when (3) {
            1, 3 -> 1 // 可以把多个分支放在一起处理
            Math.sqrt(3.toDouble()).toInt() -> 4 //表达式也可以作为分支条件
            in 1..10 -> print("x is in the range")// 检测 一个值在不在这个区间
            !in 1..10 -> print("x is not in the range")
            2 -> print("2")
            else -> {
                print("什么都没有找到")
            }
        }
        //when 也可以替代 if-else
//        when { 哪个条件为真执行哪个分支
//            x.isOdd() -> print("x is odd")
//            x.isEven() -> print("x is even")
//            else -> print("x is funny")
//        }
        //2.类
        var kot = KotlinClass("kotlin类测试")//像调用函数那样实例化类
        Thread(object : Runnable {
            override fun run() {
// 匿名内部类
            }
        })
//        2.1 java 类和kotlin 类做了区分
//        val kotlinClass : KClass<LocalDate> = LocalDate::class
//        val javaClass : Class<LocalDate> = LocalDate::class.java

    }
}
