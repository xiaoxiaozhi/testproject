package com.example.kotlin.excute

import com.myapplication.interfac.KotlinFunInterface
import com.myapplication.module.KotlinClass
import com.myapplication.module.KotlinDataClsss

/**
 * 类
 */
fun main(args: Array<String>) {
    //1.类
    var kot = KotlinClass("kotlin类测试")//像调用函数那样实例化类
    kot.p1 = "123";
    kot.no = 1;
    println("p1 = ${kot.p1} no = ${kot.no}")
    //1.1 函数式接口:注意函数式接口需要在 interface前面加 fun
    var inter = KotlinFunInterface { "" }
    //1.2 类扩展函数,注意在类外面声明
    kot.print()
    //2. 数据类
    var data = KotlinDataClsss("张三", 15)
    //2.1.1数据类-解构函数 数据类型自带解构函数，非数据类型需要加operate
    var (name, agee) = data
    //2.1.2 普通类-解构函数
    var data1 = Data("", 23)
    var (d1, d2) = data1
    var (d3, _) = data1//未使用变量,用下划线
//        2.2 java 类和kotlin 类做了区分
//        val kotlinClass : KClass<LocalDate> = LocalDate::class
//        val javaClass: Class<LocalDate> = LocalDate::class.java
    //3. 委托 一个类中定义的方法实际是调用另一个类的对象的方法来实现的
    val b = BaseImpl(10)
    Derived(b).print() // 输出 10
}

fun KotlinClass.print() {
    println("create extent function!!!")
}

class Data(var d1: String, var d2: Int) {
    operator fun component2(): String {
        return d1
    }

    operator fun component1(): Int {
        return d2
    }
}

// 创建接口
interface Base {
    fun print()
}

// 实现此接口的被委托的类
class BaseImpl(val x: Int) : Base {
    override fun print() { print(x) }
}

// 通过关键字 by 建立委托类
class Derived(b: Base) : Base by b