package com.myjetpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
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
        Log.e(Companion.TAG, "get savedInstanceState value = " + savedInstanceState?.getString("save"))
    }

    override fun onPause() {
        super.onPause()
        Log.e(Companion.TAG, "onPause");
    }

    override fun onResume() {
        super.onResume()
        Log.e(Companion.TAG, "onResume");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(Companion.TAG, "onDestroy");
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.e(Companion.TAG, "excute onSaveInstanceState");
        outState.putString("save", "123")
    }

}