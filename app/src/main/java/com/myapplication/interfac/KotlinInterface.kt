package com.myapplication.interfac

//跟Java8 类似 允许方法有默认实现：
interface KotlinInterface {
    fun bar()    // 未实现
    fun foo() {  //已实现
        // 可选的方法体
        println("foo")
    }

    var name: String //接口中的属性只能是抽象的，不允许初始化值
}