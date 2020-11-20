package com.example.kotlin.excute

fun main(args: Array<String>) {

    foo(baz = 1)
    B().foo()
    //3.中缀表示法
    println("中缀表示法${1 double 2}")
    MyStringCollection() add "sadas"//成员函数中使用中缀表示法


}

//1.默认参数的函数
fun foo(bar: Int = 0, baz: Int) {
    println("foo")
}

//2.单表达式函数
fun double(x: Int): Int = x * 2

/**
 * ## 3.中缀表示法
 * - 它们必须是成员函数或扩展函数；
 * - 它们必须只有一个参数；
 * - 其参数不得接受可变数量的参数且不能有默认值
 */

infix fun Int.double(x: Int) = x * 2

class MyStringCollection {
    infix fun add(s: String) { /*……*/
    }

    fun build() {
        this add "abc"   // 正确
        add("abc")       // 正确
        //add "abc"        // 错误：必须指定接收者
    }
}

open class A {
    open fun foo(i: Int = 10) {
        println(i)
    }
}

//3. 编码规范
fun foo1(): Int {     // 不良
    return 1
}

fun foo() = 1        // 良好

//4. 高阶函数：参数类型是函数类型，或者返回值是函数类型的函数称为高阶函数，除此之外函数类型还能赋值给变量

//可变数量函数 参数需要 vararg 修饰
fun <T> asList(vararg ts: T): List<T> {
    val result = ArrayList<T>()
    for (t in ts) // ts is an Array
        result.add(t)
    return result
}

class B : A() {
    override fun foo(i: Int) {
        super.foo(i)
    }  // 有默认参数的函数被覆盖，覆盖函数不能有默认值
}