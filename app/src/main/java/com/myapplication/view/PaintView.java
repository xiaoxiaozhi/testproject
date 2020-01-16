package com.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhixun on 2018/7/3 0003. 练习画笔类
 */

public class PaintView extends View {
    Paint paint, paint1;

    public PaintView(Context context) {
        super(context);
        init();
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo(30, 80);
        path.lineTo(180, 80);
        path.lineTo(50, 200);
        //设置线冒样式
        paint.setStrokeCap(Paint.Cap.BUTT);//没有
        canvas.drawPath(path, paint);
        canvas.drawTextOnPath("没有线冒", path, 10, -35, paint1);

        path.offset(300, 0);
        paint.setStrokeCap(Paint.Cap.ROUND);//圆形
        canvas.drawPath(path, paint);
        canvas.drawTextOnPath("圆形线冒", path, 10, -35, paint1);

        path.offset(300, 0);
        paint.setStrokeCap(Paint.Cap.SQUARE);//方形 ，跟默认一样
        canvas.drawPath(path, paint);
        canvas.drawTextOnPath("方形线冒", path, 10, -35, paint1);

        //设置拐弯
        path.offset(-580, 300);
        paint.setStrokeJoin(Paint.Join.ROUND);//方形 ，跟默认一样
        canvas.drawPath(path, paint);
        canvas.drawTextOnPath("圆形拐弯", path, 0, -35, paint1);

        path.offset(300, 0);
        paint.setStrokeJoin(Paint.Join.MITER);//方形 ，跟默认一样
        canvas.drawPath(path, paint);
        canvas.drawTextOnPath("锐角拐弯", path, 0, -35, paint1);

        path.offset(300, 0);
        paint.setStrokeJoin(Paint.Join.BEVEL);//方形 ，跟默认一样
        canvas.drawPath(path, paint);
        canvas.drawTextOnPath("方形拐弯", path, 0, -35, paint1);

        PathMeasure pathMeasure = new PathMeasure();
        super.onDraw(canvas);
    }

    void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(50);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(android.R.color.darker_gray));

        paint1 = new Paint();
        paint1.setTextSize(40);
        paint1.setAntiAlias(true);
        paint1.setColor(getResources().getColor(android.R.color.holo_blue_dark));


    }
}
