package com.myapplication.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.myapplication.utils.FGLRender;

//GLSurfaceView 继承自 SurfaceView 自带渲染线程
public class FGLView extends GLSurfaceView {
    public FGLView(Context context) {
        super(context);
    }

    //绘制三角形
    public FGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);//设置版本
        setRenderer(new FGLRender(this));//渲染过程交给FGLRender
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);//这种是按需渲染，另一种实时渲染RENDERMODE_CONTINUOUSLY
        requestRender();//设置按需渲染后，需要调用这个
    }
}
