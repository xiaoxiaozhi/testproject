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
import android.view.animation.LinearInterpolator;

/**
 *
 */
public class PathMeasureView extends View {
    Path mPath, mDst, mPathLoading, mPathLoadingDst;
    PathMeasure mPathMeasure, loadingPM;
    private Paint mPaint;
    private float mLength, loadingLength;
    private float mAnimatorValue;

    public PathMeasureView(Context context) {
        super(context);
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();
        mDst = new Path();
        mPathMeasure = new PathMeasure();

        mPathLoading = new Path();
        mPathLoadingDst = new Path();
        loadingPM = new PathMeasure();

        ValueAnimator va = ValueAnimator.ofFloat(0, 1);
        va.setDuration(1000);
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setRepeatMode(ValueAnimator.RESTART);
        va.setInterpolator(new LinearInterpolator());
        va.addUpdateListener(animation -> {
            mAnimatorValue = (float) animation.getAnimatedValue();
            invalidate();
        });
        va.start();
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath.addCircle(w / 7, h / 2, 70, Path.Direction.CW);
        mPathMeasure.setPath(mPath, true);
        mLength = mPathMeasure.getLength();

        mPathLoading.addCircle(w / 4, h / 2, 70, Path.Direction.CW);
        loadingPM.setPath(mPathLoading, true);
        loadingLength = loadingPM.getLength();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDst.reset();
        mDst.lineTo(0, 0);//android bug 如果不设置 ，getsegment可能失效
        mPathMeasure.getSegment(0, mAnimatorValue * mLength, mDst, true);
//        canvas.drawPath(mDst, mPaint);//canvas只能绘制一个path

        float stop = mAnimatorValue * loadingLength;
        float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * loadingLength));
        mPathLoadingDst.reset();
        mPathLoadingDst.lineTo(0, 0);
        mPathMeasure.getSegment(start, stop, mPathLoadingDst, true);
        canvas.drawPath(mPathLoadingDst, mPaint);
    }
}
