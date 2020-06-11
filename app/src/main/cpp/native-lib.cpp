#include <jni.h>
#include <string>
#include <malloc.h>
#include <string.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <pthread.h>
#include "x264.h"
#include "librtmp/rtmp.h"
#include "VideoChannel.h"
#include "safe_queue.h"
#include "AudioChannel.h"
#include "WangYiFFmpeg.h"
#include <android/native_window_jni.h>
#include "JavaCallHelper.h"
#include "macro.h"

JavaCallHelper *javaCallHelper;

#include "WangYiFFmpeg.h"

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
ANativeWindow *window = 0;
WangYiFFmpeg *wangYiFFmpeg;

//线程  ----》javaVM
JavaVM *javaVM = NULL;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    javaVM = vm;
    return JNI_VERSION_1_4;
}

void renderFrame(uint8_t *data, int linesize, int w, int h) {
    pthread_mutex_lock(&mutex);//停止播放的时候报错，这里要加锁
    if (!window) {
        pthread_mutex_unlock(&mutex);
        return;
    }
//    渲染
    //设置窗口属性
    ANativeWindow_setBuffersGeometry(window, w,
                                     h,
                                     WINDOW_FORMAT_RGBA_8888);

    ANativeWindow_Buffer window_buffer;
    if (ANativeWindow_lock(window, &window_buffer, 0)) {
        ANativeWindow_release(window);
        window = 0;
        pthread_mutex_unlock(&mutex);
        return;
    }
//    缓冲区  window_data[0] =255;
    uint8_t *window_data = static_cast<uint8_t *>(window_buffer.bits);
//    r     g   b  a int
    int window_linesize = window_buffer.stride * 4;
    uint8_t *src_data = data;
    for (int i = 0; i < window_buffer.height; ++i) {
        memcpy(window_data + i * window_linesize, src_data + i * linesize, window_linesize);
    }
    ANativeWindow_unlockAndPost(window);
    pthread_mutex_unlock(&mutex);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_myapplication_activity_PlayActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(av_version_info());
}extern "C"
JNIEXPORT void JNICALL
Java_com_myapplication_utils_WangyiPlayer_native_1start(JNIEnv *env, jobject thiz) {
    //    正是进入播放状态
    if (wangYiFFmpeg) {
        wangYiFFmpeg->start();
    }
}extern "C"
JNIEXPORT void JNICALL
Java_com_myapplication_utils_WangyiPlayer_native_1prepare(JNIEnv *env, jobject instance, jstring dataSource_) {
    const char *dataSource = env->GetStringUTFChars(dataSource_, 0);
    javaCallHelper = new JavaCallHelper(javaVM, env, instance);
    wangYiFFmpeg = new WangYiFFmpeg(javaCallHelper, dataSource);
    wangYiFFmpeg->setRenderCallback(renderFrame);
    wangYiFFmpeg->prepare();
    env->ReleaseStringUTFChars(dataSource_, dataSource);
}extern "C"
JNIEXPORT void JNICALL
Java_com_myapplication_utils_WangyiPlayer_native_1set_1surface(JNIEnv *env, jobject thiz, jobject surface) {
    if (window) {
        ANativeWindow_release(window);
        window = 0;
    }
//创建新的窗口用于视频显示
    window = ANativeWindow_fromSurface(env, surface);
}extern "C"
JNIEXPORT jint JNICALL
Java_com_myapplication_utils_WangyiPlayer_native_1getDuration(JNIEnv *env, jobject thiz) {
    if (wangYiFFmpeg) {
        return wangYiFFmpeg->getDuration();
    }
    return 0;
}extern "C"
JNIEXPORT void JNICALL
Java_com_myapplication_utils_WangyiPlayer_native_1seek(JNIEnv *env, jobject thiz, jint progress) {
    if (wangYiFFmpeg) {
        wangYiFFmpeg->seek(progress);
    }
}extern "C"
JNIEXPORT void JNICALL
Java_com_myapplication_utils_WangyiPlayer_native_1stop(JNIEnv *env, jobject thiz) {
    if (wangYiFFmpeg) {
        wangYiFFmpeg->stop();
    }
    if (javaCallHelper) {
        delete javaCallHelper;
        javaCallHelper = 0;
    }
}extern "C"
JNIEXPORT void JNICALL
Java_com_myapplication_utils_WangyiPlayer_native_1release(JNIEnv *env, jobject thiz) {
    if (window) {
        ANativeWindow_release(window);
        window = 0;
    }
}extern "C"
JNIEXPORT void JNICALL
Java_com_myapplication_utils_WangyiPlayer_native_1pose(JNIEnv *env, jobject thiz) {
    if (wangYiFFmpeg) {
        wangYiFFmpeg->pose();
    }
}extern "C"
JNIEXPORT void JNICALL
Java_com_myapplication_utils_WangyiPlayer_native_1restart(JNIEnv *env, jobject thiz) {
    if (wangYiFFmpeg) {
        wangYiFFmpeg->restart();
    }
}