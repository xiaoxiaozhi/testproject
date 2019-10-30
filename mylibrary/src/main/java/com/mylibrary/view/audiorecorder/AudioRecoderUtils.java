package com.mylibrary.view.audiorecorder;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import com.mylibrary.utils.MyLog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MarioStudio on 2016/5/12.
 */

public class AudioRecoderUtils {

    private String filePath;
    private MediaRecorder mMediaRecorder;
    private final String TAG = "MediaRecord";
    public static final int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;

    VoicePathListener pathListener;
    private boolean isRecording = false;
    /**
     * 存放录音的文件夹
     */
    private String fileDir;
    private OnAudioStatusUpdateListener audioStatusUpdateListener;

    public AudioRecoderUtils() {
        this.filePath = "/dev/null";
    }

    private String path;

    public AudioRecoderUtils(File file) {
        this.filePath = file.getAbsolutePath();
    }

    /**
     * @param path
     * @param file 文件夹存放录好的音频
     */
    public AudioRecoderUtils(VoicePathListener path, String file) {
        fileDir = file;
        pathListener = path;
    }

    private long startTime;
    private long endTime;

    /**
     * 开始录音 使用amr格式
     * 录音文件
     *
     * @return
     */
    public void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        if (!isRecording) {
            try {
            /* ②setAudioSource/setVedioSource */
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            /* ③准备 */
                if (filePath != null) {
                    mMediaRecorder.setOutputFile(filePath);
                } else {
                    path = fileDir + File.separator + getCurrentDate() + ".amr";
                    mMediaRecorder.setOutputFile(path);
                }

                mMediaRecorder.setMaxDuration(MAX_LENGTH);
                mMediaRecorder.prepare();
            /* ④开始 */
                mMediaRecorder.start();
                // AudioRecord audioRecord.
            /* 获取开始时间* */
                startTime = System.currentTimeMillis();
                updateMicStatus();
                Log.i("ACTION_START", "startTime" + startTime);
            } catch (IllegalStateException e) {
                Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
            } catch (IOException e) {
                Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
            }
            isRecording = true;
        }
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        if (mMediaRecorder == null)
            return;
        if (isRecording) {
            endTime = System.currentTimeMillis();
            Log.i("ACTION_END", "endTime" + endTime);
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            isRecording = false;
            Log.i("ACTION_LENGTH", "Time" + (endTime - startTime));
            if (endTime - startTime < 1300) {//因为操作有300毫秒的延迟
                pathListener.onPath(null);
            } else {
                MyLog.i("录音路径", path);
                pathListener.onPath(path);
            }
        }
    }

    private final Handler mHandler = new Handler();

    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };

    /**
     * 更新话筒状态
     */
    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != audioStatusUpdateListener) {
                    audioStatusUpdateListener.onUpdate(db);
                }
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    public interface OnAudioStatusUpdateListener {
        public void onUpdate(double db);
    }

    public interface VoicePathListener {
        /**
         * path ==null 说明录音时间少于1秒
         *
         * @param path
         */
        public void onPath(String path);
    }

    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }
}
