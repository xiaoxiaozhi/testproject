package com.myapplication.donghua;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 贝塞尔曲线-路径变换
 */
public class BezierChangeView extends View {
    private float mStartPointX, mStartPointY, mEndPointX, mEndPointY;
    private float mFlagPointX, mFlagPointY;//控制点
    private Path mPath;
    private Paint mPaintBezier;
    private Paint mPaintFlag;
    private Context context;


    public BezierChangeView(Context context) {
        super(context);
    }

    public BezierChangeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mPaintBezier = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBezier.setStrokeWidth(3);
        mPaintBezier.setColor(context.getResources().getColor(android.R.color.holo_blue_bright));
        mPaintBezier.setStyle(Paint.Style.STROKE);

        mPaintFlag = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFlag.setStrokeWidth(3);
        mPaintFlag.setStyle(Paint.Style.STROKE);
    }

    public BezierChangeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BezierChangeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartPointX = w / 4;
        mStartPointY = h / 4;
        mEndPointX = w * 3 / 4;
        mEndPointY = h / 4;
        mFlagPointX = w / 2;
        mPath = new Path();
        ValueAnimator va = ValueAnimator.ofFloat(h / 4, h - 30);
        va.setDuration(3000);
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setRepeatMode(ValueAnimator.REVERSE);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFlagPointY = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        va.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(mStartPointX, mStartPointY);
        mPath.quadTo(mFlagPointX, mFlagPointY, mEndPointX, mEndPointY);//绝对坐标，绘制二阶贝塞尔曲线
//        mPath.rQuadTo();//相对坐标
        canvas.drawPath(mPath, mPaintBezier);
        canvas.drawCircle(mStartPointX, mStartPointY, 5, mPaintFlag);
        canvas.drawCircle(mEndPointX, mEndPointY, 5, mPaintFlag);
        canvas.drawCircle(mFlagPointX, mFlagPointY, 5, mPaintFlag);

        canvas.drawLine(mStartPointX, mStartPointY, mFlagPointX, mFlagPointY, mPaintFlag);
        canvas.drawLine(mFlagPointX, mFlagPointY, mEndPointX, mEndPointY, mPaintFlag);
    }
}
