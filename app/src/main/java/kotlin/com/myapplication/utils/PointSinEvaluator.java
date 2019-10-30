package kotlin.com.myapplication.utils;

import android.animation.TypeEvaluator;
import android.graphics.Point;
import android.graphics.PointF;

public class PointSinEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        PointF startPoint = (PointF) startValue;
        PointF endPoint = (PointF) endValue;
        float x = startPoint.x + fraction * (endPoint.x - startPoint.x);
        float y = (float) (Math.sin(x * Math.PI / 180) * 100) + endPoint.y / 2;
        PointF point = new PointF(x, y);
        return point;
    }
}
