package com.mylibrary.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//javac是编译java程序的工具，一个普通的.exe的文件
//注解处理器是运行在它自己的JVM中。javac启动一个完整Java虚拟机来运行注解处理器，这意味着你可以使用任何你在其他java应用中使用的的东西。
//两种元注解 ：Retention有三种值分别对应源代码source class 和运行时；默认值为class
// target 表示注解用在什么地方：method ;file用于描述域; constructor; type用于描述类、注解、接口等 默认值为任何元素(成分)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface MyAnnotation {
    /**
     * 为注解添加属性
     *
     * @return
     */
    String color() default "blue";

    String value();//　如果一个注解中有一个名称为value的属性，

    // 且你只想设置value属性(即其他属性都采用默认值或者你只有一个value属性)，那么可以省略掉“value=”部分。

    /**
     * 数组类型的属性
     *
     * @return
     */
    int[] array() default {1, 2, 3};

}
