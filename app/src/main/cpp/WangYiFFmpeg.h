//
// Created by Administrator on 2019/6/4.
//

#ifndef PALYERWANGYI_WANGYIFFMPEG_H
#define PALYERWANGYI_WANGYIFFMPEG_H

#include <pthread.h>
#include <android/native_window.h>
#include "VideoChannel.h"
#include "AudioChannel.h"
#include "BaseChannel.h"

extern "C" {
#include <libavformat/avformat.h>
#include <libavutil/time.h>
}

//控制层
class WangYiFFmpeg {
public:
    int duration;

    WangYiFFmpeg(JavaCallHelper *javaCallHelper, const char *dataSource);

    ~WangYiFFmpeg();

    void prepare();

    void prepareFFmpeg();

    void start();

    void play();

    void setRenderCallback(RenderFrame renderFrame);

    int getDuration();

    void seek(int progress);

    void stop();


    bool isPlaying;
    char *url;
    pthread_t pid_prepare;//销毁
    pthread_t pid_play;//知道播放完毕
    pthread_t pid_stop;//释放线程
    VideoChannel *videoChannel;
    AudioChannel *audioChannel;
    AVFormatContext *formatContext;
    JavaCallHelper *javaCallHelper;
    RenderFrame renderFrame;
    /**
     * 默认没有拖动
     */
    bool isSeek = 0;
    /**
     * 线程锁，在构造方法中初始化
     */
    pthread_mutex_t seekMutex;//
    void pose();

    void restart();
};


#endif //PALYERWANGYI_WANGYIFFMPEG_H
