package com.myapplication.donghua;

import android.animation.TypeEvaluator;

/**
 * 自定义估值器，具体实现参见自定义view SineView
 */
public class MyEvaluator implements TypeEvaluator<Point> {
    /**
     * @param fraction   插值器getInterpolation（）的返回值 ,表示动画完成度（根据它来计算当前动画的值）
     * @param startValue 动画的初始值
     * @param endValue   动画的结束值
     * @return
     */
    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        // 将动画初始值startValue 和 动画结束值endValue 强制类型转换成Point对象
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;

        // 根据fraction来计算当前动画的x和y的值
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());

        // 将计算后的坐标封装到一个新的Point对象中并返回
        Point point = new Point(x, y);
        return point;
    }
}
