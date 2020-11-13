package com.myapplication.module

/**
 * 1.构造函数
 * kotlin类有一个主构造函数以及一个或多个次级构造函数。柱构造函数位于类名之后
 * 如果类没有注解也没有任何可见性修饰符，constructor可以省略class KotlinClass (name:String){}
 * 2.修饰符
 */
class KotlinClass constructor(name: String) {
    //1.次构造函数, 可以通过this 代理主构造函数
    constructor(num: Int) : this(num.toString()) {
        //1.1创建匿名内部类
        object : claz3("123") {
            override fun pr() {
                super.pr()
            }
        }
    }

    //2.属性声明 field 代表的变量本身，交后端变量，只能用于属性访问器
    var p1: String = ""
        get() {
            field.toString()
            return "$field sadnjskdfn asfd"
        }
        set(value) {
            field = value
        }
    var no: Int = 10
        get() = field
        set(value) {
            if (value < 10) {
                field = value;
            } else {
                field = -1
            }
        }
    private lateinit var p2: String

    init {
        //初始化代码 可以使用主构造函数的变量
        println("main constructor const name = $name")
        claz7().pr()
    }

    //嵌套类
    class claz1 {
        var num: Int = 11;
        fun pr() {
//            print("$p1")//嵌套类不能使用外部类的属性。 报错
        }
    }

    //内联类，内敛累可以使用 外部类的属性
    inner class claz2 {
        fun pr() {
            print("$p1")
        }
    }

    //可继承类，kotlin 类和方法默认是final不可继承，如果想要被继承 需要加open关键字
    open class claz3(name: String) {
        var str: String = "123"//子类不能有该属性，除非open修饰 属性重载
        open var num: Int = 0
            set(value) {
                field = value
            }

        open fun pr() {
            println("print claz3")
        }
    }

    interface interface1 {
        fun pr() {
            println("print interface1")
        }
    }

    //抽象类 参考Java的抽象类 ,不能创建他的实体类
    abstract class claz4 {
        abstract fun pr()
    }

    //继承 如果子类有主构造函数，则必须立即初始化父类。
    class claz5(name: String) : claz3(name)

    //继承 如果子类没有主构造函数，则必须在每一个二级构造函数中用 super 关键字初始化基类
    class claz6 : claz3 {
        constructor(str: String) : super(str) {
        }
    }

    //继承--函数重载
    class claz7 : claz3, interface1 {
        constructor() : super("")

        override fun pr() {
            super<claz3>.pr()
            super<interface1>.pr()
        }
    }

    //继承--属性重载,子类中不能有跟父类属性名一样的属性，除非使用属性重载
    class claz8 : claz3 {
        constructor() : super("123")

        override var num: Int = 0
    }
}