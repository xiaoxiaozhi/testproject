package com.myjetpack

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

//lifecycle具体事件参考 https://developer.android.com/topic/libraries/architecture/lifecycle#java
class MyObserve(lifecycle: Lifecycle) : LifecycleObserver {
    companion object {
        const val TAG: String = "MyObserve"
    }

    private var lifeCycle = lifecycle;

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectListener() {
        Log.i(TAG, "onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() {
        Log.i("MyObserve", "ON_PAUSE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (lifeCycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            //判断当前 处于什么生命周期
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {

    }
}