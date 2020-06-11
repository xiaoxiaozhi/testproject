package com.myapplication.utils;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WangyiPlayer implements SurfaceHolder.Callback {
    private String dataSource;

    private native void native_prepare(String dataSource);

    private native void native_start();

    private native void native_pose();
    private native void native_restart();

    private native void native_set_surface(Surface surface);

    private native int native_getDuration();

    private native void native_seek(int progress);

    private SurfaceHolder surfaceHolder;

    public void setSurfaceView(SurfaceView surfaceView) {
        if (null != this.surfaceHolder) {
            this.surfaceHolder.removeCallback(this);
        }
        this.surfaceHolder = surfaceView.getHolder();
        this.surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        native_set_surface(surfaceHolder.getSurface());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void prepare() {
        native_prepare(dataSource);
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }


    private OnPrepareListener onPrepareListener;

    private OnErrorListener onErrorListener;
    private OnProgressListener onProgressListener;

    public void setOnPrepareListener(OnPrepareListener onPrepareListener) {
        this.onPrepareListener = onPrepareListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    //    不断调用
    public void onProgress(int progress) {
        if (null != onProgressListener) {
            onProgressListener.onProgress(progress);
        }

    }

    public void onPrepare() {
        if (null != onPrepareListener) {
            onPrepareListener.onPrepared();
        }

    }

    //
    public void onError(int errorCode) {
        if (null != onErrorListener) {
            onErrorListener.onError(errorCode);
        }

    }

    public void start() {
        native_start();
    }

    public void stop() {
        native_stop();
    }

    //activity消失时调用
    public void release() {
        if (this.surfaceHolder != null) {
            this.surfaceHolder.removeCallback(this);
        }
        native_release();
    }

    private native void native_stop();

    private native void native_release();

    public int getDuration() {
        return native_getDuration();
    }

    public void seek(int progress) {
        native_seek(progress);
    }

    public void pose() {

    }

    public void pause() {
        native_pose();
    }

    public void restart() {
        native_restart();
    }


    public interface OnPrepareListener {
        void onPrepared();
    }

    public interface OnErrorListener {
        void onError(int error);
    }

    public interface OnProgressListener {
        void onProgress(int progress);
    }
}
