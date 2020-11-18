package com.myapplication.interfac

//只有一个 抽象方法的接口叫函数式接口，此例是bar 。可以用lambd表达式实例化 例如， KotlinInterface{"123"},不允许有抽象属性
fun interface KotlinFunInterface {
    fun bar(): String    // 未实现
    fun foo() {  //已实现
        // 可选的方法体
        println("foo")
    }

//    companion object {
//        const val name: String = "123"
//    }
}