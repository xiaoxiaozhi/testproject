package kotlin.com.myapplication.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SuccessView extends View {
    Paint paint;
    int padding = 10;
    int angle;
    ValueAnimator animator;

    public SuccessView(Context context) {
        super(context);
        init();
    }

    public SuccessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SuccessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        float left = getLeft();
        float right = getRight();
        float bottom = getBottom();
        float top = getTop();

        // 计算successView的长宽，确定圆心位置
        float x = (getRight() - getLeft()) / 2;
        float y = (getBottom() - getTop()) / 2;
        //先确定矩形形状然后计算各边位置
        float min = Math.min(right - left, bottom - top);
        left = x - min / 2 + padding;
        right = x + min / 2 - padding;
        top = y - min / 2 + padding;
        bottom = y + min / 2 - padding;
        path.addArc(left, top, right, bottom, 0, angle);
        if (animator == null) {
            animator = ValueAnimator.ofInt(0, 365);//要比360大一点，图像看起来才能愈合
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setDuration(2 * 1000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    angle = (int) animation.getAnimatedValue();
//                    postInvalidateDelayed(1000);
                    invalidate();
                }
            });
//            animator.setStartDelay(1000);//解决不了残影问题
            animator.start();
        }
        canvas.drawPoint(x, y, paint);
        canvas.drawPath(path, paint);

    }

    void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(getResources().getColor(android.R.color.holo_blue_light));
    }
}
