package com.myapplication.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.myapplication.R

/**
 * 参考 https://juejin.im/post/5e184f43f265da3df860f65c
 * 需要在清单中申请 <uses-feature
android:glEsVersion="0x00020000"
android:required="true"></uses-feature>
 */
class OpenGLESActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gles)
    }
}
