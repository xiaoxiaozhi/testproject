package com.myapplication.donghua;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myapplication.R;
import com.myapplication.view.SineView;

//https://www.jianshu.com/p/420629118c10   动画总结
public class DhActivity extends AppCompatActivity implements View.OnClickListener {
    TextView drdc, scale;
    ImageView zdh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dh);
//        ;
//        addContentView(new SineView(this), new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 200));
        //传统动画 ：帧动画    动画文件是一个drawable在drawable文件夹（好像只针对ImageView存疑）
        zdh = (ImageView) findViewById(R.id.zdh);
        zdh.setImageResource(R.drawable.zhen);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) zdh.getDrawable();
        animationDrawable1.start();

        //传统动画 ：补间动画  动画文件就是一个动画在anim文件夹   有四种 淡入淡出
        drdc = (TextView) findViewById(R.id.drdc);
        drdc.setOnClickListener(this);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        Log.i("动画的hash", "" + animation.hashCode());
        animation.setRepeatMode(Animation.RESTART);//设置动画模式为重复
//        animation.setRepeatCount(Animation.INFINITE);再次animation.setRepeatCount(Animation.INFINITE); 动画会无限执行
        drdc.setTag(animation);
        drdc.startAnimation(animation);

        // 缩放
        addAnimation(R.id.scale, R.anim.scale);
        //旋转
        addAnimation(R.id.rotate, R.anim.rotate);
        //位移
        addAnimation(R.id.translate, R.anim.translate);
//        ObjectAnimator oa = ObjectAnimator.ofFloat(findViewById(R.id.drdc1),"alpha",)
//        ValueAnimator va =ValueAnimator.ofFloat()
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
}
