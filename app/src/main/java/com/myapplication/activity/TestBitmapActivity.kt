package com.myapplication.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.text.InputFilter
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.WindowManager
import android.widget.EditText

import com.mylibrary.utils.MaxTextLengthFilter
import com.mylibrary.utils.MyInputFilter

import java.util.Random

import com.myapplication.R

class TestBitmapActivity : BaseActivity() {
    override fun fullScreen(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_bitmap)
        val et = findViewById<EditText>(R.id.text)
        et.filters = arrayOf<InputFilter>(MaxTextLengthFilter(et, 3))

        //        for (int i = 0; i < 20; i++) {
        //            Log.i("随机数", String.valueOf(new Random().nextInt(5)));
        //        }
    }

}
