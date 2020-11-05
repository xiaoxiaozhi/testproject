package com.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;

public class ArcView extends View {
    public ArcView(Context context) {
        super(context);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //圆弧  先确定矩形，drawRect方法会以这个矩形中心开始画弧。
        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setStrokeWidth(1);//笔画宽度
        paint1.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, getWidth(), getHeight()/2, paint1);


        RectF oval = new RectF(-getWidth()/2, -getHeight()-2,
                getWidth()*1.5f, getHeight() - 2);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);//笔画宽度
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(android.R.color.darker_gray));

        canvas.drawArc(oval, 0, 180, false, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
