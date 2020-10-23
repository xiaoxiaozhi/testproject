package com.myapplication.utils;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.myapplication.MyApplication;


public class TtsUtils {
    public static String TAG = "TtsUtils";
    private TextToSpeech mTextToSpeech;
    private long startTime;
    private static TtsUtils ttsUtils;

    public void speak(String playText) {
        if (mTextToSpeech != null) {
            Log.e(TAG, " playText ready to play:" + playText);
            mTextToSpeech.speak(playText, TextToSpeech.QUEUE_ADD, null, "systemttsid");
        } else {
            Log.e(TAG, " playText txttospeech null");
        }
    }

    public void stop() {
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
        } else {
            Log.e(TAG, " stopPlay txttospeech null");
        }
    }

    public static TtsUtils getInstance() {
        if (ttsUtils == null) {
            Log.e(TAG, "tts getinstance");
            ttsUtils = new TtsUtils();
        } else {
            Log.e(TAG, "tts instance exist");
        }
        return ttsUtils;
    }

    public TtsUtils() {
        initTts();
    }

    public void initTts() {
        try {
            mTextToSpeech = new TextToSpeech(MyApplication.Companion.getInstance(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        Log.e(TAG, "system tts init success");
                        mTextToSpeech.setPitch(1.0f);//max-girl  min-boy
                        mTextToSpeech.setSpeechRate(1.0f); //1.5f  4.7word/second  0.5f 2.4word/second
                        mTextToSpeech.setOnUtteranceProgressListener(mttsProgressListener);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "init exception:" + e.getMessage());
        }
        Log.e(TAG, "tts init finish!");
//        int result = synthesizer.speak("行自侠已开启");
    }


    private UtteranceProgressListener mttsProgressListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String s) {
            startTime = System.currentTimeMillis();
            Log.e(TAG, " systemtts start:");
        }

        @Override
        public void onDone(String s) {
            long lastTime = System.currentTimeMillis() - startTime;
            Log.e(TAG, " systemtts done:" + s + " lasttime:" + lastTime);
        }

        @Override
        public void onError(String s) {
            Log.e(TAG, " systemtts error:" + s);
        }
    };
}
