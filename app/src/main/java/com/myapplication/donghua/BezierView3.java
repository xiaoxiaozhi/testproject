package com.myapplication.donghua;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BezierView3 extends View {
    private float mStartPointX, mStartPointY, mEndPointX, mEndPointY;
    private float mFlagPointX1, mFlagPointY1, mFlagPointX2, mFlagPointY2;//控制点
    private Path mPath;
    private Paint mPaintBezier;
    private Paint mPaintFlag;
    private Context context;
    private boolean isSecondPoint;

    public BezierView3(Context context) {
        super(context);
    }

    public BezierView3(Context context, @Nullable AttributeSet attrs) {
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

    public BezierView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BezierView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartPointX = w / 4;
        mStartPointY = h / 2;

        mEndPointX = w * 3 / 4;
        mEndPointY = h / 2;

        mFlagPointX1 = w / 2;
        mFlagPointY1 = h / 5;

        mFlagPointX2 = w / 2 + 100;
        mFlagPointY2 = h * 3 / 4;
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(mStartPointX, mStartPointY);
        mPath.cubicTo(mFlagPointX1, mFlagPointY1, mFlagPointX2, mFlagPointY2, mEndPointX, mEndPointY);//绝对坐标，绘制三阶贝塞尔曲线
//        mPath.rQuadTo();//相对坐标
        canvas.drawPath(mPath, mPaintBezier);
        canvas.drawCircle(mStartPointX, mStartPointY, 5, mPaintFlag);
        canvas.drawCircle(mEndPointX, mEndPointY, 5, mPaintFlag);
        canvas.drawCircle(mFlagPointX1, mFlagPointY1, 5, mPaintFlag);
        canvas.drawCircle(mFlagPointX2, mFlagPointY2, 5, mPaintFlag);

        canvas.drawLine(mStartPointX, mStartPointY, mFlagPointX1, mFlagPointY1, mPaintFlag);
        canvas.drawLine(mFlagPointX1, mFlagPointY1, mFlagPointX2, mFlagPointY2, mPaintFlag);
        canvas.drawLine(mFlagPointX2, mFlagPointY2, mEndPointX, mEndPointY, mPaintFlag);
//        canvas.drawColor(context.getResources().getColor(android.R.color.white));
    }

    /**
     * 多点触控
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                //按下第二根手指
                isSecondPoint = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                isSecondPoint = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mFlagPointX1 = event.getX(0);
                mFlagPointY1 = event.getY(0);
                if (isSecondPoint) {
                    mFlagPointX2 = event.getX(1);
                    mFlagPointY2 = event.getY(1);
                }
                invalidate();
                break;
        }
        return true;
    }
}
