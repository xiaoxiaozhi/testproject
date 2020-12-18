package com.example.kotlin.excute

/**
 * 官方文档->函数 翻译太差看不懂
 *
 */
fun main(args: Array<String>) {

    foo(baz = 1)
    B().foo()
    //3.中缀表示法
    println("中缀表示法${1 double 2}")
    MyStringCollection() add "sadas"//成员函数中使用中缀表示法

    println(lambda1(10, 5))
//    A::foo(1)
}

//1.默认参数的函数
fun foo(bar: Int = 0, baz: Int) {
    println("fun foo default arg bar = $bar baz = $baz ")
}

//1.1可变数量函数 参数需要 vararg 修饰
fun <T> asList(vararg ts: T): List<T> {
    val result = ArrayList<T>()
    for (t in ts) // ts is an Array
        result.add(t)
    return result
}

//1.2有默认参数的函数被覆盖，覆盖函数不能有默认值
class B : A() {
    override fun foo(i: Int) {
        super.foo(i)
    }  //
}

open class A {
    open fun foo(i: Int = 10) {
        println(i)
    }
}

//2.单表达式函数
fun double(x: Int): Int = x * 2
fun double1(x: Int) = x * 2

/**
 * ## 3.中缀表示法
 * - 它们必须是成员函数或扩展函数；
 * - 它们必须只有一个参数；
 * - 其参数不得接受可变数量的参数且不能有默认值
 */

infix fun Int.double(x: Int) = x * 2

class MyStringCollection {
    infix fun add(s: String) { /*……*/
        println(s)
    }

    fun build() {
        this add "abc"   // 正确
        add("abc")       // 正确
        //add "abc"        // 错误：必须指定接收者
    }
}


//3. 编码规范
fun foo1(): Int {     // 不良
    return 1
}

fun foo() = 1        // 良好

//4. 高阶函数：参数类型是函数类型，或者返回值是函数类型的函数称为高阶函数，除此之外函数类型还能赋值给变量
<<<<<<< HEAD
//扔物线lambda https://zhuanlan.zhihu.com/p/126498955
=======
//仍物线 https://zhuanlan.zhihu.com/p/126498955
//4.1 函数作为参数或返回：不同参数的函数不是一个类型，所以在声明高阶函数的时候要指定传参和返回
fun a(funParame: () -> Unit): (Int, String) -> String {
    var b = ::parameFun//函数对象化，当参数传入或者返回时要加双冒号
//    b(1,"2")等同于 b.invoke(1,"2") //
    return ::parameFun//用双冒号吧函数作为一个对象传递
}


fun parameFun(i: Int, s: String): String {
    return ""
}

//4.2 匿名函数：匿名函数不是函数，是一个对象
var anonymousFun = fun() = 1

>>>>>>> 394803f8b564cfb2a10d867a6cc88e57dcbf65c8
//4.1  lambda 表达式-函数类型来声明函数 :()内是参数->之后是函数返回类新型
var lambda1: (Int, Int) -> Int = { x, y -> x - y }

//4.1  lambda 表达式-函数类型来声明函数 :带有接收者的函数
//var lambda2: Math.(Int) -> Unit = { x.(u) -> x-u }
//4.2 lambda 表达式-挂起函数
//var lambda2: Math.(Int) -> Unit = { x.(u) -> x-u }
//
