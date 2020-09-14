package com.myapplication.binder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 *android 基于文件的形式实现进程通信：dev目录下 binder 文件
 * 内存映射：将用户空间的一段内存映射到内核空间，映射成功后，用户对这段内存区域的修改可以直接映射到内核空间，同样内核空间修改也直接映射到用户空间
 *          适用于需要 频繁读写文件的场景。
 * mmap：内存映射，正常情况下操作文件 用户空间---》内核空间---》磁盘文件。 用了mmap后简单理解，用户空间直接操控磁盘文件
 */
class DavidService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
