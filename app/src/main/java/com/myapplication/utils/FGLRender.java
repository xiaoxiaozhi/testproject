package com.myapplication.utils;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.View;

import com.myapplication.view.FGLView;
import com.myapplication.view.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FGLRender implements GLSurfaceView.Renderer {
    public View mView;
    Triangle triangle;

    public FGLRender(View view) {
        mView = view;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0, 0, 0, 0);//清空为黑色
        triangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    //不断被调用，requestRender触发
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        triangle.onDrawFrame(gl);
    }
}
