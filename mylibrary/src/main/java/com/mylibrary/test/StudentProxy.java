package com.mylibrary.test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class StudentProxy implements Person {
    //设计一个类变量记住代理类要代理的目标对象
    private Person ldh = new Student();

    public Person getProxy() {
        Person p = (Person) Proxy.newProxyInstance(StudentProxy.class.getClassLoader(),
                Student.class.getInterfaces(),
                (Object proxy, Method method, Object[] args) -> {
                    if (method.getName().equals("say")) {
                        return method.invoke(ldh, args);
                    }
                    return null;
                });
        return p;
    }

    @Override
    public void say(String s) {

    }
}
