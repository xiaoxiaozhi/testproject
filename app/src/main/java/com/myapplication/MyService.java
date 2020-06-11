package com.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import java.io.UnsupportedEncodingException;

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
        Log.i(TAG, "count::" + count);

    }

    private static LruCache<String, String> stringCache;

    public static String getString4cache(String id) {
        if (stringCache == null) {
            int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            int cacheSize = maxMemory / 8;
            LruCache<String, String> stringCache = new LruCache<String, String>(cacheSize) {
                @Override
                protected int sizeOf(String key, String value) {
                    try {
                        return value.getBytes("UTF-8").length;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            };
        }
        return stringCache.get(id);
    }

    public static void setString2cache(String id, String value) {

    }

}
