package com.myapplication.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;

import com.mylibrary.view.CustomScrollView;

import com.myapplication.R;

public class DrawBitmapView extends View {
    private Resources mResources;
    private Paint mBitPaint;
    private Bitmap mBitmap;
    private Rect mSrcRect, mDestRect;

    // view 的宽高
    private int mTotalWidth, mTotalHeight;

    public DrawBitmapView(Context context) {
        this(context, null);

    }

    public DrawBitmapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawBitmapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mResources = getResources();
        initBitmap();
        initPaint();
    }


    private void initPaint() {
        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
    }

    private void initBitmap() {
        mBitmap = ((BitmapDrawable) mResources.getDrawable(R.mipmap.ic_launcher))
                .getBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect src = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        Rect des = new Rect(100, 10, mBitmap.getWidth()+100, mBitmap.getHeight()+10);
        canvas.drawBitmap(mBitmap, src, des, new Paint());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
    }
}

