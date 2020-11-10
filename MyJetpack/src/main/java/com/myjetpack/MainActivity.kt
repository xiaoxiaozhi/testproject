package com.myjetpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

//activity实现接口LifecycleOwner后，可以添加生命周期观察者。
class MainActivity : AppCompatActivity(), LifecycleOwner {
    companion object {
        const val TAG = "MainActivity";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(MyObserve(lifecycle))
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause");
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy");
    }
}