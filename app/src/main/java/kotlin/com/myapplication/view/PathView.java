package kotlin.com.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhixun on 2018/7/2 0002.
 */

public class PathView extends View {
    Paint paint, paint1;
    private final static float SIZE = 50f;

    public PathView(Context context) {
        super(context);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(android.R.color.black));
        paint.setTextSize(SIZE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        LinearGradient linearGradient = new LinearGradient(10, 10, 6 * 50, 10, Color.GREEN, Color.BLUE, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);

        paint1 = new Paint();
        paint1.setColor(getResources().getColor(android.R.color.holo_blue_light));
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(5);
        paint1.setTextSize(SIZE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path p = new Path();
//        p.moveTo(100, 20);
        Path path = new Path();
        Path path1 = new Path();
        Path path2 = new Path();
        Path path3 = new Path();
        path.moveTo(100, 100);//起点
        path.lineTo(500, 100);//这个起点是相当于 100,100
        path.lineTo(600, 150);
        path.close();
        canvas.drawTextOnPath("你是一个好人", path, 0, -8, paint);
        canvas.drawPath(path, paint);
        path.offset(0, 50);
        canvas.drawPath(path, paint);
        canvas.drawText("你是一个好人", 10, paint.getTextSize(), paint);


        path1.rMoveTo(100, 100);
        path1.rLineTo(0, 100);//相对于rMoceTo 的坐标加上 人LineTo的坐标。rLineTo(0,300)可看做是点（100,100）到点（100,400）之间的线段
        canvas.drawPath(path1, paint);

        RectF rectF = new RectF(600, 300, 1000, 600);
        path2.addRect(rectF, Path.Direction.CCW);
        canvas.drawPath(path2, paint);
        canvas.drawTextOnPath("天气不错", path2, 0, -8, paint);

        for (int i = 1; i < 5; i++) {
            path3.addCircle(100 * i, 400, 80, Path.Direction.CW);
        }
//        path3.setFillType(Path.FillType.EVEN_ODD);
        canvas.drawPath(path3, paint1);
        super.onDraw(canvas);
    }
}
