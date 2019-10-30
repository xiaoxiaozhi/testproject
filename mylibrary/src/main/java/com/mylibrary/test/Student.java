package com.mylibrary.test;
@MyAnnotation("value属性比较特殊，在注解里面加上后所有加了该注解的类都要给一个默认值")
public class Student implements Person {
    @Override
    public void say(String s) {
        MyAnnotation ma = (MyAnnotation) Student.class.getAnnotation(MyAnnotation.class);//取不到实例，奇怪了
        System.out.println("Student 有注解实例对象" + ma.color());
        System.out.println("我是一名" + s + "学生");
    }
}
