package com.mylibrary.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


/**
 * Created by zhixun on 2018/3/19 0019.
 * 按日期分组列表装饰
 */
public abstract class AxisDecoration extends RecyclerView.ItemDecoration {
    private Paint p, pt, dashedPaint;
    private int divider, fh;
    private Paint.FontMetrics fm;
    private Bitmap complete, doing, unfinish;
    private int dividerT = 100, dividerB = 0, dividerL = 150, dividerR = 100;
    private Context context;

    public AxisDecoration(Context context) {
        p = new Paint();
        p.setTextAlign(Paint.Align.CENTER);
        dashedPaint = new Paint();
        dashedPaint.setColor(0xff319281);
        dashedPaint.setAntiAlias(true);
        dashedPaint.setDither(true);
        dashedPaint.setStrokeWidth(5f);
        dashedPaint.setPathEffect(new DashPathEffect(new float[]{10f, 10f}, 0));

        pt = new Paint();
        pt.setColor(0xff319281);
        pt.setAntiAlias(true);
        pt.setDither(true);
        pt.setTextSize(25);

//        p.setColor(context.getResources().getColor(R.color.dividerColor));
//        pt.setColor(0xff999999);
//        pt.setTextSize(context.getResources().getDimension(R.dimen.ssd));
//        divider = (int) context.getResources().getDimension(R.dimen.divider);
        fm = pt.getFontMetrics();
        this.context = context;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            float top = view.getTop() - divider;
//            Log.i("top", i + "  " + view.getTop());
            float bottom = view.getBottom();
            if (isShowItemLabel(parent.getChildAdapterPosition(view))) {
//                c.drawBitmap();
                c.drawBitmap(getBitmap(parent.getChildAdapterPosition(view)),
                        dividerL / 2, view.getTop(), p);
//get
//                c.drawRect(view.getLeft(), top, view.getRight(), view.getTop(), p);
                c.drawText(getItemLabelStr(parent.getChildAdapterPosition(view)), view.getLeft() + view.getPaddingLeft(), top + (divider) / 2 + pt.descent() + 3, pt);
            }

        }
        c.drawLine(dividerL / 2, parent.getTop() - dividerT, dividerL / 2, parent.getBottom() + dividerB, dashedPaint);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    /**
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        if (isShowItemLabel(parent.getChildAdapterPosition(view))) {
//            outRect.top = divider;
//        }
//        if((BaseRecyclerAdapter<SyItem>)parent.getAdapter()).getItem(parent.getChildAdapterPosition(view)).isTop()
        //相当于magin
        outRect.top = dividerT;
        outRect.left = dividerL;
        outRect.right = dividerR;
    }

    /**
     * 是否显示Item的标签
     * 字类必须要复写此方法来说明当前item是否要显示标签
     *
     * @param position 当前item的索引
     */
    protected abstract boolean isShowItemLabel(int position);

    /**
     * 获取标签类型
     *
     * @param position 当前item的索引
     */
    protected abstract boolean getItemType(int position);

    /**
     * 获取标签类型
     *
     * @param position 当前item的索引
     */
    protected abstract Bitmap getBitmap(int position);

    /**
     * 获取标签的字符串
     * 字类必须要复写此方法来返回标签的文本内容
     *
     * @param position 当前item的索引
     */
    protected abstract String getItemLabelStr(int position);

    /**
     * 设置已完成图片
     *
     * @param rid 图片R id
     * @return
     */
    AxisDecoration setComplete(int rid) {
//        this.complete = context.getResources().getDrawable(rid);
        complete = BitmapFactory.decodeResource(context.getResources(), rid);
        return this;
    }

    /**
     * 设置执行中图片
     *
     * @param rid
     * @return
     */
    AxisDecoration setdoing(int rid) {
        doing = BitmapFactory.decodeResource(context.getResources(), rid);
        return this;
    }

    /**
     * 设置未完成图片
     *
     * @param rid
     * @return
     */
    AxisDecoration setUnfinish(int rid) {
        unfinish = BitmapFactory.decodeResource(context.getResources(), rid);
        return this;
    }
}
