package com.myapplication.utils;

import android.animation.TypeEvaluator;
import android.graphics.Point;
import android.graphics.PointF;

public class PointSinEvaluator implements TypeEvaluator<PointF> {

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        PointF startPoint =  startValue;
        PointF endPoint =  endValue;
        float x = startPoint.x + fraction * (endPoint.x - startPoint.x);
        float y = (float) (Math.sin(x * Math.PI / 180) * 100) + endPoint.y / 2;
        PointF point = new PointF(x, y);
        return point;
    }
}
