package com.myapplication.test;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 反射
 * 1. constructors 目的：在运行时得到某一个类的实例
 * 2.filed
 * 3.method
 */
public class ReflectTest {
    public final static String[] MODIFY = {"0", "public", "private", "3", "protected"};

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String... args) {


//        T1.class.getConstructors();//只能获取public 的构造函数
        //打印构造函数
        for (int i = 0; i < T1.class.getDeclaredConstructors().length; i++) {//获取全部构造函数
            Constructor<T1> constructor = (Constructor<T1>) T1.class.getDeclaredConstructors()[i];
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < constructor.getParameterCount(); j++) {
                sb.append(constructor.getParameterTypes()[j].getSimpleName() + " " + constructor.getParameters()[j].getName());
                sb.append(",");
            }
            System.out.println(MODIFY[constructor.getModifiers()] + " " + T1.class.getSimpleName() + "(" + sb.deleteCharAt(sb.length() - 1).toString() + ")");
//            System.out.println(constructor.toGenericString());//直接打印
            sb.setLength(0);
        }
        // 使用构造函数
        try {
            Constructor<T1> constructor = T1.class.getDeclaredConstructor(String.class);//使用固定的构造函数
            constructor.setAccessible(true);//如果是public 就不用 设置可访问权限
            System.out.println(constructor.newInstance("张三").name);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //构造函数的一些方法
        try {
            Constructor<T1> constructor = T1.class.getConstructor(String.class, String.class);
            System.out.println("从构造函数得到class名称：" + constructor.getDeclaringClass().getName());
//getGenericParameterTypes 与 getParameterTypes 都是获取构成函数的参数类型，前者返回的是Type类型，后者返回的是Class类型，由于Type顶级接口，
// Class也实现了该接口，因此Class类是Type的子类，Type 表示的全部类型而每个Class对象表示一个具体类型的实例，如String.class仅代表String类型。
// 由此看来Type与 Class 表示类型几乎是相同的，只不过 Type表示的范围比Class要广得多而已
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        System.out.println("getFields()只能获取所有public字段,包括父类字段");
        //filed
        for (int i = 0; i < T1.class.getFields().length; i++) {
            System.out.print(T1.class.getFields()[i].getName() + "\t");//	只能获取所有public字段,包括父类字段
        }
        System.out.println("");
        System.out.println("getDeclaredFields()获取所有字段,public和protected和private,但是不包括父类字段");
        for (int i = 0; i < T1.class.getDeclaredFields().length; i++) {
            System.out.print(T1.class.getDeclaredFields()[i].getName() + "\t");//获取所有字段,public和protected和private,但是不包括父类字段
        }
        //给Field赋值
        System.out.println("");
        try {
            T1 t1 = T1.class.newInstance();
            Field field = T1.class.getDeclaredField("age");//private字段  会报错
            field.set(t1, 13);
            System.out.println("年龄" + t1.age);
//            field.wait();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static class T1 extends T2 {
        private String name, occupation = "";
        private int age;


        private T1(String name) {
            this.name = name;
        }

        private T1(int age) {
            this.age = age;
        }

        public T1(String name, String occupation) {
            this.name = name;
            this.occupation = occupation;
        }

        protected T1(String name, String occupation, int age) {
            this.name = name;
            this.occupation = occupation;
            this.age = age;
        }

        @Override
        public String toString() {
            return "姓名:" + this.name + "\t职业:" + this.occupation;
        }
    }

    public static class T2 {
        public String address;

    }
}
