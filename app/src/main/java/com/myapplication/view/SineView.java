package com.myapplication.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PointFEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.myapplication.donghua.MyInterpolator;
import com.myapplication.utils.PointSinEvaluator;

/**
 * Created by zhixun on 2018/6/21 0021.
 */

public class SineView extends View {
    Paint paint, linePaint, paintTxt;
    PointF curP;
    ValueAnimator va;
    int paintColor = 0xff000000;

    //    ViewGroup
    public SineView(Context context) {
        super(context);
        init();
    }

    public SineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(paintColor);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(getResources().getColor(android.R.color.holo_blue_light));
        linePaint.setStrokeWidth(3);

        paintTxt = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTxt.setTextSize(30);
        paintTxt.setStyle(Paint.Style.FILL);
        paintTxt.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i("onLayout", "onLayout");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("onSizeChanged", "onSizeChanged");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i("onFinishInflate", "onFinishInflate");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("onMeasure", "onMeasure");
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.i("onDraw", "onDraw");
        //获取该控件的宽高，画一个圆让它移动到控件的最右端
        //创建一个属性动画
        //创建一个插值器
        //在动画回调事件中重新画图
        if (va == null) {
            PointF start = new PointF(20f, 20f);
            PointF end = new PointF(getWidth() - 20f, getHeight() - 20f);
            va = ValueAnimator.ofObject(new PointSinEvaluator(), start, end);
            va.setDuration(5 * 1000);
            va.setRepeatCount(ValueAnimator.INFINITE);
            va.setRepeatMode(ValueAnimator.RESTART);
//            va.start();
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    curP = (PointF) animation.getAnimatedValue();
                    invalidate();
//                    postInvalidateDelayed(100);
                }
            });
            va.setInterpolator(new MyInterpolator());//实现插值器
            ObjectAnimator oa = ObjectAnimator.ofFloat(this, "radius", 20f, 50f, 20f);
            oa.setRepeatCount(ValueAnimator.INFINITE);
            oa.setRepeatMode(ValueAnimator.RESTART);

            ObjectAnimator oa1 = ObjectAnimator.ofObject(this,
                    "paintColor", new ArgbEvaluator(),
                    getResources().getColor(android.R.color.holo_green_dark),
                    0xffff4081);
            oa1.setRepeatCount(ValueAnimator.INFINITE);
            oa1.setRepeatMode(ValueAnimator.RESTART);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(5 * 1000);
            animatorSet.playTogether(oa, va, oa1);
            animatorSet.start();
        }
        if (curP != null) {
            paint.setColor(paintColor);
            canvas.drawCircle(curP.x, curP.y, radius, paint);
        } else {
            canvas.drawCircle(0, (getBottom() - getTop()) / 2, radius, paint);
        }
//        canvas.drawRect(10,10,getWidth()-10,getHeight()-10,linePaint);
        canvas.drawLine(10, getHeight() / 2, getWidth(), getHeight() / 2, linePaint);
        canvas.drawLine(10, 10, 10, getHeight() - 10, linePaint);
        canvas.drawText("动画合集:sin函数估值器、ArgbEvaluator颜色渐变估值器", 50f, 50f, paintTxt);
        super.onDraw(canvas);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
    }


    float radius = 20;
}
