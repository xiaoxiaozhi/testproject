package com.myapplication.interfac

//跟Java8 类似 允许方法有默认实现：
//只有一个 抽象方法的接口叫函数式接口，此例是bar 。可以用lambd表达式实例化 例如， KotlinInterface{"123"}
interface KotlinInterface {
    fun bar(): String    // 未实现
    fun foo() {  //已实现
        // 可选的方法体
        println("foo")
    }

    var name: String //接口中的属性只能是抽象的，不允许初始化值
}