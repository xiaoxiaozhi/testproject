package com.myapplication.module

import com.myapplication.interfac.KotlinInterface

class KotlinImpl : KotlinInterface {
    override fun bar(): String {
        return ""
    }

    override var name: String
        get() = "123"
        set(value) {}
}