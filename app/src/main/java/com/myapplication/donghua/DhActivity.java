package com.myapplication.donghua;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myapplication.R;
import com.myapplication.view.SineView;

//https://www.jianshu.com/p/2412d00a0ce4 属性动画
public class DhActivity extends AppCompatActivity implements View.OnClickListener {
    TextView drdc, scale;
    ImageView zdh;

    //    static {
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dh);
//        addContentView(new SineView(this), new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 200));
        //传统动画 ：帧动画    动画文件是一个drawable在drawable文件夹（好像只针对ImageView存疑）
        zdh = (ImageView) findViewById(R.id.zdh);
        zdh.setImageResource(R.drawable.zhen);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) zdh.getDrawable();
        animationDrawable1.start();

        //传统动画 动画文件就是一个动画在anim文件夹   有四种 淡入淡出
        //补间动画
        addAnimation(R.id.drdc, R.anim.alpha_anim);
        //缩放
        addAnimation(R.id.scale, R.anim.scale);
        //旋转
        addAnimation(R.id.rotate, R.anim.rotate);
        //位移
        addAnimation(R.id.translate, R.anim.translate);
        //属性动画 动画文件放在animator文件夹中，或者代码实现
        objectAnimator();
        valueAnimator();
        //属性动画-估值器:控制值的具体变化
        // 正弦曲线
        //属性动画-插值器

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(getClass().getSimpleName(), "执行onResume");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drdc:
                // Animation a =  v.getAnimation(); //不清楚为什么一直为空
                v.startAnimation((Animation) v.getTag());
                break;
            default:
                v.startAnimation((Animation) v.getTag());
                break;
        }
    }

    View addAnimation(int viewId, int animId) {
        TextView textView = (TextView) findViewById(viewId);
        Animation animation = AnimationUtils.loadAnimation(this, animId);
        animation.setRepeatMode(Animation.RESTART);
        textView.startAnimation(animation);
        textView.setTag(animation);
        textView.setOnClickListener(this);
        return textView;
    }

    void objectAnimator() {
        TextView myView = (TextView) findViewById(R.id.drdc1);
//        ObjectAnimator anim = ObjectAnimator.ofFloat(myView, "alpha", 1.0f, 0.8f, 0.6f, 0.4f, 0.2f, 0.0f);
//        anim.setRepeatCount(-1);
//        anim.setRepeatMode(ObjectAnimator.REVERSE);
//        anim.setDuration(2000);
//        anim.start();
        //属性动画组
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(myView, "alpha", 1.0f, 0.5f, 0.8f, 1.0f);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(myView, "scaleX", 0.0f, 1.0f);
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(myView, "rotation", 0, 360);
        ObjectAnimator transXAnim = ObjectAnimator.ofFloat(myView, "translationX", 100, 400);
        ObjectAnimator transYAnim = ObjectAnimator.ofFloat(myView, "tranlsationY", 100, 750);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alphaAnim, scaleXAnim, rotateAnim, transXAnim, transYAnim);
//        set.playTogether(alphaAnim, scaleXAnim, scaleYAnim, rotateAnim);
        set.setDuration(3000);
        set.start();
    }

    /**
     * 代码实现和xml实现
     */
    void valueAnimator() {
        TextView value = findViewById(R.id.value);
// 步骤1：设置动画属性的初始值 & 结束值
        ValueAnimator anim = ValueAnimator.ofFloat(1f, 0.8f, 0.5f, 0f, 0.4f, 0.5f, 1f);
// 步骤2：设置动画的播放各种属性
        anim.setDuration(1500);
        // 设置动画运行的时长

        anim.setStartDelay(500);
        // 设置动画延迟播放时间

        anim.setRepeatCount(ValueAnimator.INFINITE);
        // 设置动画重复播放次数 = 重放次数+1
        // 动画播放次数 = infinite时,动画无限重复

        anim.setRepeatMode(ValueAnimator.RESTART);
        // 设置重复播放动画模式
        // ValueAnimator.RESTART(默认):正序重放
        // ValueAnimator.REVERSE:倒序回放

// 步骤3：将改变的值手动赋值给对象的属性值：通过动画的更新监听器
        // 设置 值的更新监听器
        // 即：值每次改变、变化一次,该方法就会被调用一次
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                // 获得改变后的值
                System.out.println(currentValue);
                // 输出改变后的值
                // 步骤4：将改变后的值赋给对象的属性值，下面会详细说明
                value.setAlpha(currentValue);
                // 步骤5：刷新视图，即重新绘制，从而实现动画效果
                value.requestLayout();
            }
        });

        anim.start();
        // 启动动画

        TextView xmlValue = findViewById(R.id.animatorXml);
        ValueAnimator animator = (ValueAnimator) AnimatorInflater.loadAnimator(this, R.animator.animator);
// 载入XML动画
        animator.setTarget(xmlValue);
// 设置动画对象
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                // 获得改变后的值
                System.out.println(currentValue);
                // 输出改变后的值
                // 步骤4：将改变后的值赋给对象的属性值，下面会详细说明
                xmlValue.getLayoutParams().width = currentValue;
                // 步骤5：刷新视图，即重新绘制，从而实现动画效果
                xmlValue.requestLayout();
            }
        });
        animator.start();
// 启动动画

    }
}
