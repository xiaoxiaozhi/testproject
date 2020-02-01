package com.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * zai manifest中注册<service android:name=".MyService"></service>
 * https://www.cnblogs.com/it-tsz/p/11601265.html
 */
public class MyService extends IntentService {
    private static final String TAG = MyService.class.getSimpleName();

    private int count = 0;

    public MyService() {
        super(TAG);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        //在这里添加我们要执行的代码，Intent中可以保存我们所需的数据，
        //每一次通过Intent发送的命令将被顺序执行
        count++;
        Log.w(TAG, "count::" + count);
    }
}
