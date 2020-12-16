package com.myapplication.module

/**
 * 1.数据类 必须有 一个主构造函数
 * 2.所有的主构造函数的参数必须标识为val 或者 var ;
 * 3.数据类不可以声明为 abstract, open, sealed 或者 inner;
 * 4.数据类不能继承其他类 (但是可以实现接口)。
 */
data class KotlinDataClsss(var name: String, var age: Int) {
}