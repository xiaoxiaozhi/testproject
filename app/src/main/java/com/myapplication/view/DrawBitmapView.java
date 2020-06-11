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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.View;

import com.mylibrary.view.CustomScrollView;

import com.myapplication.R;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * android 3.0-8.0 bitmap 内存模型在java虚拟机上受gc管理强调复用来解决内存优化问题，其他版本 native层，调用recycle()回收.
 * https://www.jianshu.com/p/780fb6ca2a3f 方法详解
 * 1. bitmap复用
 * 2. https://www.cnblogs.com/nimorl/p/8065071.html BitmapFactory.Options详解
 * 3. bitmap优化：比如一张图片一亿像素手机分辨率根本没有怎么高，但是这张位图占据的内存就是一亿像素的内存这时候要压缩位图
 * 4. 三级缓存：https://www.jianshu.com/p/70d954168c93
 */
public class DrawBitmapView extends View {
    private Resources mResources;
    private Paint mBitPaint, paint;
    private Bitmap mBitmap;
    private Rect src, des;

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
        paint = new Paint();
    }

    private void initBitmap() {

        TypedValue value = new TypedValue();
        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inDensity 位图的像素密度 翻看源码   final TypedValue value = new TypedValue();
        //            is = res.openRawResource(id, value); value.density获取位图的像素密度如果获取不到默认160
        //options.inTargetDensity  被画出来的像素密度，设备的像素密度
        options.inMutable = true;//位图能被复用
        options.inSampleSize = 1;// 按倒数缩放
        options.inTargetDensity = value.density;//不缩放，显示原始尺寸
        options.inScaled = false;//true，缩放两次，decodeResource自动缩放一次公式bitmap.(w或h)*options.inTargetDensity/options.inDensity ，然后按照inSampleSize倒数缩放 ；false，缩放一次decodeResource获取原值再按照inSampleSize倒数缩放
//        InputStream ins = getResources().openRawResource(R.drawable.record_animate_01);
//        BitmapFactory.decodeResourceStream()
        //通过这种方式获得的bitmap都会被压缩，压缩原理https://blog.csdn.net/qiantanlong/article/details/87712906
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.record_animate_01, options);
        Log.i("bitmap", "inTargetDensity：" + options.inTargetDensity + " inDensity：" + options.inDensity);
        Log.i("bitmap", "宽：" + mBitmap.getWidth() + " 高：" + mBitmap.getHeight());
        src = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        des = new Rect(100, 10, mBitmap.getWidth() + 100, mBitmap.getHeight() + 10);
        //b

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, src, des, paint);
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

    /**
     * 一、获取缩放后的本地图片
     *
     * @param filePath 文件路径
     * @param width    宽
     * @param height   高
     * @return
     */
    public static Bitmap readBitmapFromFile(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);//不在你村中加载，只获取宽高
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if (srcHeight > height || srcWidth > width) {//TODO 为什么要缩放
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);// 取最大的整数，例如0.5 在0和1之间 取1
            } else {
                inSampleSize = Math.round(srcWidth / width);//取最大的整数，例如-0.5 在-1 和0 之间去最大的 0
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 二、获取缩放后的本地图片 效率高于一
     *
     * @param filePath 文件路径
     * @param width    宽
     * @param height   高
     * @return
     */
    public static Bitmap readBitmapFromFileDescriptor(String filePath, int width, int height) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            int inSampleSize = 1;

            if (srcHeight > height || srcWidth > width) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / height);
                } else {
                    inSampleSize = Math.round(srcWidth / width);
                }
            }

            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;

            return BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * 三、从输入流中获取本地图片
     *
     * @param ins    输入流
     * @param width  宽
     * @param height 高
     * @return
     */
    public static Bitmap readBitmapFromInputStream(InputStream ins, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ins, null, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeStream(ins, null, options);
    }

    /**
     * 三、从resource资源加载图片
     *
     * @param resources
     * @param resourcesId
     * @param width
     * @param height
     * @return
     */
    @Deprecated
    public static Bitmap readBitmapFromResource(Resources resources, int resourcesId, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //这种方式加载的图片会根据不同分辨率缩放
        BitmapFactory.decodeResource(resources, resourcesId, options);//
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeResource(resources, resourcesId, options);
    }
}

