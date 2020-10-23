package com.myapplication.donghua;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * PathMeasure切线
 */
public class PathPOSTanView extends View implements View.OnClickListener {
    private Path mPath;
    private float[] mPos = new float[2];
    private float[] mTan = new float[2];
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private ValueAnimator va;
    private float currentValue;

    public PathPOSTanView(Context context) {
        super(context);
    }

    public PathPOSTanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();

        va = ValueAnimator.ofFloat(0, 1);
        va.setDuration(2000);
        va.setRepeatMode(ValueAnimator.RESTART);
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.addUpdateListener(animation -> {
            currentValue = (float) animation.getAnimatedValue();
            invalidate();
        });
        va.start();
//        setOnClickListener(this);
    }

    public PathPOSTanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath.addCircle(w / 2, h / 2, h / 3, Path.Direction.CW);
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPathMeasure.getPosTan(currentValue * mPathMeasure.getLength(), mPos, mTan);//mPos获取某一个点的切线mTan 切线坐标
        float degree = (float) (Math.atan2(mTan[0], mTan[1]) * 180 / Math.PI);//根据切线坐标获取角度
        canvas.save();
//        canvas.translate(100, 100);//平移画布到100,100的点
        canvas.drawCircle(mPos[0], mPos[1], 10, mPaint);
        canvas.drawPath(mPath, mPaint);
//        canvas.rotate(degree);
        canvas.drawLine(getWidth() + getHeight() / 3, getHeight() / 3, getWidth() + getHeight() / 3, getHeight() / 3 + 100,mPaint);
        canvas.restore();

    }

    @Override
    public void onClick(View v) {
        va.start();
    }
}
