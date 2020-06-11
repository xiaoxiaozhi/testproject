//
// Created by Administrator on 2019/6/4.
//

#include "WangYiFFmpeg.h"
#include "JavaCallHelper.h"
#include "macro.h"

void *prepareFFmpeg_(void *args) {
    WangYiFFmpeg *wangYiFFmpeg = static_cast<WangYiFFmpeg *>(args);
    wangYiFFmpeg->prepareFFmpeg();
    return 0;
}

WangYiFFmpeg::WangYiFFmpeg(JavaCallHelper *javaCallHelper, const char *dataSource) {
    url = new char[strlen(dataSource) + 1];
    this->javaCallHelper = javaCallHelper;
    strcpy(url, dataSource);
    pthread_mutex_init(&seekMutex, 0);//在构造方法中初始化锁,动态初始化
}

WangYiFFmpeg::~WangYiFFmpeg() {
    pthread_mutex_destroy(&seekMutex);//销毁锁
    DELETE(javaCallHelper);
    free(url);
}

void WangYiFFmpeg::prepare() {
//
    pthread_create(&pid_prepare, NULL, prepareFFmpeg_, this);
}

void WangYiFFmpeg::prepareFFmpeg() {
    /*子线程
     * 访问到对象的属性*/
    avformat_network_init();
    // 代表一个 视频/音频 包含了视频、音频的各种信息
    formatContext = avformat_alloc_context();
    //1、打开URL
    AVDictionary *opts = NULL;
    //设置超时3秒
    av_dict_set(&opts, "timeout", "3000000", 0);
    //强制指定AVFormatContext中AVInputFormat的。这个参数一般情况下可以设置为NULL，这样FFmpeg可以自动检测AVInputFormat。
    //输入文件的封装格式
//    av_find_input_format("avi");// rtmp
//    av_find_input_format("rtmp");
    int ret = avformat_open_input(&formatContext, url, NULL, &opts);
    char errbuf[128];
    if (av_strerror(ret, errbuf, sizeof(errbuf)) == 0) {
        LOGE("打开视频输入流信息失败，失败原因： %s", errbuf);
    }

    if (ret != 0) {
        javaCallHelper->onError(THREAD_CHILD, FFMPEG_CAN_NOT_OPEN_URL);
        return;
    }
    //2.查找流
    if (avformat_find_stream_info(formatContext, NULL) < 0) {
        if (javaCallHelper) {
            javaCallHelper->onError(THREAD_CHILD, FFMPEG_CAN_NOT_FIND_STREAMS);
        }
        return;
    }
    duration = formatContext->duration / 1000000;
    LOGE("第一次%d", duration);
    for (int i = 0; i < formatContext->nb_streams; ++i) {
        AVCodecParameters *codecpar = formatContext->streams[i]->codecpar;
        AVStream *stream = formatContext->streams[i];

        //找到解码器
        AVCodec *dec = avcodec_find_decoder(codecpar->codec_id);
        if (!dec) {
            if (javaCallHelper) {
                javaCallHelper->onError(THREAD_CHILD, FFMPEG_FIND_DECODER_FAIL);
            }
            return;
        }
        //创建上下文
        AVCodecContext *codecContext = avcodec_alloc_context3(dec);
        if (!codecContext) {
            if (javaCallHelper)
                javaCallHelper->onError(THREAD_CHILD, FFMPEG_ALLOC_CODEC_CONTEXT_FAIL);
            return;
        }
        //复制参数
        if (avcodec_parameters_to_context(codecContext, codecpar) < 0) {
            if (javaCallHelper)
                javaCallHelper->onError(THREAD_CHILD, FFMPEG_CODEC_CONTEXT_PARAMETERS_FAIL);
            return;
        }
        //打开解码器
        if (avcodec_open2(codecContext, dec, 0) != 0) {
            if (javaCallHelper)
                javaCallHelper->onError(THREAD_CHILD, FFMPEG_OPEN_DECODER_FAIL);
            return;
        }
        //音频
        if (codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
//音频
            audioChannel = new AudioChannel(i, javaCallHelper, codecContext, stream->time_base);

        } else if (codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            AVRational frame_rate = stream->avg_frame_rate;
//视频
//            int fps = frame_rate.num / (double)frame_rate.den;
            int fps = av_q2d(frame_rate);
//            1/fps
            videoChannel = new VideoChannel(i, javaCallHelper, codecContext, stream->time_base);
            videoChannel->setRenderCallback(renderFrame);
            videoChannel->setFps(fps);

        }
    }
    LOGE("有多少流 %d", formatContext->nb_streams);//播放视频获得两个流，rtmp还没试
    //音视频都没有
    if (!audioChannel && !videoChannel) {
        if (javaCallHelper)
            javaCallHelper->onError(THREAD_CHILD, FFMPEG_NOMEDIA);
        return;
    }
    videoChannel->audioChannel = audioChannel;
    if (javaCallHelper)
        javaCallHelper->onParpare(THREAD_CHILD);

}

void *startThread(void *args) {

    WangYiFFmpeg *ffmpeg = static_cast<WangYiFFmpeg *>(args);
    ffmpeg->play();
    return 0;
}

void WangYiFFmpeg::start() {
    isPlaying = true;
    if (audioChannel) {
        audioChannel->play();
    }
    if (videoChannel) {
        videoChannel->play();
    }
    pthread_create(&pid_play, NULL, startThread, this);
}

void WangYiFFmpeg::play() {
    int ret = 0;
    while (isPlaying) {
//        100帧
        if (audioChannel && audioChannel->pkt_queue.size() > 100) {
//            思想    队列    生产者的生产速度 远远大于消费者的速度  10ms   100packet     12ms  10ms
            av_usleep(1000 * 10);
            continue;
        }

        if (videoChannel && videoChannel->pkt_queue.size() > 100) {
            av_usleep(1000 * 10);
            continue;
        }

        //读取包
        AVPacket *packet = av_packet_alloc();
        // 从媒体中读取音频、视频包
        ret = av_read_frame(formatContext, packet);

//         是你读文件packet   看录播课程   FFmpeg  代码  5s  奔溃       1000帧   渲染  延时
        if (ret == 0) {
            //将数据包加入队列
            if (audioChannel && packet->stream_index == audioChannel->channelId) {
                audioChannel->pkt_queue.enQueue(packet);
            } else if (videoChannel && packet->stream_index == videoChannel->channelId) {
                videoChannel->pkt_queue.enQueue(packet);
            }
        } else if (ret == AVERROR_EOF) {
            //读取完毕 但是不一定播放完毕
            if (videoChannel->pkt_queue.empty() && videoChannel->frame_queue.empty() &&
                audioChannel->pkt_queue.empty() && audioChannel->frame_queue.empty()) {
                LOGE("播放完毕。。。");
                break;
            }
            //因为seek 的存在，就算读取完毕，依然要循环 去执行av_read_frame(否则seek了没用...)
        } else {
            break;
        }
    }

    isPlaying = 0;
    audioChannel->stop();
    videoChannel->stop();

}

void WangYiFFmpeg::setRenderCallback(RenderFrame renderFrame) {
    this->renderFrame = renderFrame;
}

int WangYiFFmpeg::getDuration() {
    return duration;
}

void WangYiFFmpeg::seek(int progress) {
    //确定seekbar拖动范围
    if (progress < 0 || progress >= duration) {
        return;
    }
    if (!formatContext) {
        return;
    }
    pthread_mutex_lock(&seekMutex);
    isSeek = 1;
    int64_t seek = progress * 1000000;
    //-1 代表音频流和视频流一起拖动，AVSEEK_FLAG_BACKWARD异步拖动
    av_seek_frame(formatContext, -1, seek, AVSEEK_FLAG_BACKWARD);
    //清空paket 和frame 队列
    if (audioChannel) {
        audioChannel->stopWork();
        audioChannel->clear();
        audioChannel->startWork();
    }
    if (videoChannel) {
        videoChannel->stopWork();
        videoChannel->clear();
        videoChannel->startWork();
    }
    pthread_mutex_unlock(&seekMutex);
    isSeek = 0;
}

void *async_stop(void *args) {
    WangYiFFmpeg *ffmpeg = static_cast<WangYiFFmpeg *>(args);
    pthread_join(ffmpeg->pid_prepare, 0);//阻塞当前线程，等执行完再续上
    ffmpeg->isPlaying = 0;
    pthread_join(ffmpeg->pid_play, 0);
    DELETE(ffmpeg->audioChannel)//会执行 audioChannel 里面的析构方法
    DELETE(ffmpeg->videoChannel)
    if (ffmpeg->formatContext) {
        avformat_close_input(&ffmpeg->formatContext);
        avformat_free_context(ffmpeg->formatContext);
        ffmpeg->formatContext = NULL;

    }
    DELETE(ffmpeg)
    LOGE("释放");
    return 0;
    //连接和分离线程
    // pthread_join(&ffmpeg->pid_prepare, 0);
}

void WangYiFFmpeg::stop() {
    isPlaying = 0;
    if (audioChannel) {
        audioChannel->stopWork();
        audioChannel->clear();
    }
    if (videoChannel) {
        videoChannel->stopWork();
        videoChannel->clear();
    }
    javaCallHelper = 0;

    if (audioChannel) {
        audioChannel->javaCallHelper = 0;
    }
    if (videoChannel) {
        videoChannel->javaCallHelper = 0;
    }

    pthread_create(&pid_stop, 0, async_stop, this);
}

void WangYiFFmpeg::pose() {
    if (audioChannel) {
        audioChannel->isPause = true;
    }
    if (videoChannel) {
        videoChannel->isPause = true;
    }

}

void WangYiFFmpeg::restart() {
    if (audioChannel) {
        audioChannel->isPause = false;
    }
    if (videoChannel) {
        videoChannel->isPause = false;
    }
}






