package com.myapplication.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
;
import android.os.Bundle;
;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.myapplication.R;

import org.jetbrains.annotations.NotNull;

/**
 * 富文本参考<a href="https://www.jianshu.com/p/f004300c6920">https://www.jianshu.com/p/f004300c6920</a>
 */
public class RichTextActivity extends BaseActivity {
    @Override
    public boolean statusVisible() {
        return true;
    }

    @NotNull
    @Override
    public void initToolbar(@NotNull Toolbar toolbar) {
        toolbar.setTitle("富文本展示");
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().getName(),"onCreate");
        setContentView(R.layout.activity_rich_text);
        TextView rich = findViewById(R.id.rich);
        //SpannableStringBuilder 可以拼接
        //SpannableString不可以拼接
        SpannableStringBuilder ssb = new SpannableStringBuilder("12345,上山打老虎,");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF4CBDFF"));
        //Spannable. SPAN_EXCLUSIVE_EXCLUSIVE 区间应用
        //Spannable.SPAN_INCLUSIVE_EXCLUSIVE 区间前应用
        //Spannable. SPAN_EXCLUSIVE_INCLUSIVE 区间后应用
        //Spannable. SPAN_INCLUSIVE_INCLUSIVE 区间前后应用 实测 这几个没什么用处，实际效果都是[)

        ssb.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                Toast.makeText(RichTextActivity.this, "富文本你点击", Toast.LENGTH_SHORT).show();
            }
        }, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//先设置点击事件,再设置文本颜色，否则文本颜色不起效
        ssb.setSpan(colorSpan, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.append("老虎打不到  ");//想要在文本末尾加图片。加至少一个空格
        Drawable drawable = getResources().getDrawable(R.drawable.navigate);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ssb.setSpan(new ImageSpan(drawable), ssb.length() - 1, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        rich.setText(ssb);
        rich.setMovementMethod(LinkMovementMethod.getInstance());

    }

}
