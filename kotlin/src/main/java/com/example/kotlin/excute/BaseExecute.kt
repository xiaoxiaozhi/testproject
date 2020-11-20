/**
 *基础语法
 */
fun main(args: Array<String>) {
    //1.基础语法
    //类型后面加?表示可为空
    var age: Int? = 12
    //如果是空抛出空指针异常
    val ages = age!!.toInt()
    //如果空返回 null
    val ages1 = age?.toInt()
    for (i in 4..1 step 2) print(i) // 输出“42”
    //1.1 when 表达式，代替了switch when既可以当表达式使用也可以作为语句，when作为表达式的时候必须有else分支
    val s = when (3) {
        1, 3 -> {
            1
            println("back 1")
        } // 可以把多个分支放在一起处理
        Math.sqrt(3.toDouble()).toInt() -> 4 //表达式也可以作为分支条件
        in 1..10 -> print("x is in the range")// 检测 一个值在不在这个区间
        !in 1..10 -> print("x is not in the range")
        2 -> print("2")
        else -> {
            print("什么都没有找到")
        }
    }
    println("what is when back $s")//返回结果 kotlin.Unit 当一个函数没有返回值的时候，我们用Unit来表示这个特征
    //when 也可以替代 if-else
//        when { 哪个条件为真执行哪个分支
//            x.isOdd() -> print("x is odd")
//            x.isEven() -> print("x is even")
//            else -> print("x is funny")
//        }
}

