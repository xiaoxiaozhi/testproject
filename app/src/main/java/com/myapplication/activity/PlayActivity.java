package com.myapplication.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.myapplication.R;
import com.myapplication.utils.WangyiPlayer;

import java.io.File;

public class PlayActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {


    WangyiPlayer wangyiPlayer;
    private SeekBar seekBar;
    private TextView current, total;
    private int progress;
    private boolean isTouch;
    private boolean isSeek;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            seekBar.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager
                .LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_player);
        current = findViewById(R.id.current);
        total = findViewById(R.id.total);
        SurfaceView surfaceView = findViewById(R.id.playSurface);
        seekBar = findViewById(R.id.seek);
        seekBar.setOnSeekBarChangeListener(this);
        wangyiPlayer = new WangyiPlayer();
        wangyiPlayer.setSurfaceView(surfaceView);
        File file = new File(Environment.getExternalStorageDirectory(), "input2.mp4");
        wangyiPlayer.setDataSource(file.getAbsolutePath());
//        wangyiPlayer.setDataSource("rtmp://192.168.0.106:1935/myapp");
//        wangyiPlayer.setDataSource("rtmp://58.200.131.2:1935/livetv/hunantv");
        wangyiPlayer.setOnPrepareListener(new WangyiPlayer.OnPrepareListener() {
            @Override
            public void onPrepared() {
                Log.i("线程名城", Thread.currentThread().getName());
                Log.i("时长", wangyiPlayer.getDuration() + "");
                if (wangyiPlayer.getDuration() != 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.seekLinear).setVisibility(View.VISIBLE);
                            total.setText(getDate(new Integer(wangyiPlayer.getDuration())));
                        }
                    });
                }
                wangyiPlayer.start();
            }
        });
        wangyiPlayer.setOnProgressListener(new WangyiPlayer.OnProgressListener() {
            @Override
            public void onProgress(int progress2) {
                Log.i("touch", "run" + progress2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int duration = wangyiPlayer.getDuration();
                        //如果是直播
                        if (duration != 0) {
                            if (isSeek) {
                                isSeek = false;
                                return;
                            }
                            seekBar.setProgress(progress2);
                            current.setText(getDate(progress2));
//                            current.setText(getDate(new Integer(progress2 * 100 / duration)));
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isTouch = true;
    }

    //拖动完再对视频进行操作
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isTouch = false;
        //得到当前进度
        Log.i("值", " wangyiPlayer.getDuration()" + wangyiPlayer.getDuration() + " 比例" + (seekBar.getProgress() / (float) seekBar.getMax()));
        Log.i("最后的值", wangyiPlayer.getDuration() * (seekBar.getProgress() / (float) seekBar.getMax()) + "");
        progress = Math.round(wangyiPlayer.getDuration() * (seekBar.getProgress() / (float) seekBar.getMax()));
        Log.i("当前进度", "" + progress);

        wangyiPlayer.seek(progress);
    }

    public void play(View view) {
        wangyiPlayer.prepare();
    }

    public void stop(View view) {
        wangyiPlayer.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wangyiPlayer != null) {
            wangyiPlayer.pose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wangyiPlayer != null) {
            wangyiPlayer.stop();
            wangyiPlayer.release();
        }
    }

    public String getDate(Integer date) {
        int h = date / 3600;
        int m = (date % 3600) / 60;
        int s = (date % 3600) % 60;
        return h + ":" + m + ":" + s;
    }

    public void pause(View view) {
        if (wangyiPlayer != null) {
            wangyiPlayer.pause();
        }
    }

    public void restart(View view) {
        if (wangyiPlayer != null) {
            wangyiPlayer.restart();
        }
    }
}
