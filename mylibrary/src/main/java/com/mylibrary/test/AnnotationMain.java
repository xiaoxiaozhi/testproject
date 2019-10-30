package com.mylibrary.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@MyAnnotation(color = "red", value = "特殊的value属性")
public class AnnotationMain {
    public static void main(String[] args) {
        StudentProxy sp = new StudentProxy();
        sp.getProxy().say("五道杠");
//        new Student();
        if (Student.class.isAnnotationPresent(MyAnnotation.class)) {
            //这叫运行注解处理器
            MyAnnotation ma = (MyAnnotation) AnnotationMain.class.getAnnotation(MyAnnotation.class);//可以取到实例因为在TestMain里面执行
//            MyAnnotation ma = (MyAnnotation) Student.class.getAnnotation(MyAnnotation.class);//取不到实例，因为没有在Student没有被实例化
            System.out.println("AnnotationMain 有注解实例对象" + ma.color());
        }
//        System.out.print(EnumColor.valueOf("RED"));
//        for (EnumColor c : EnumColor.values()) {
//            System.out.println(c);
//        }

    }
}
