package com.myapplication.activity;

import android.app.Activity;
;
import android.os.Bundle;

import com.myapplication.R;
import com.myapplication.view.CheckableLinearLayout;

/**
 * 自定义view 参考系列文章 https://www.jianshu.com/p/146e5cec4863
 */
public class CustomActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
//        CheckableLinearLayout checkableLinearLayout = findViewById(R.id.checked1);
//        checkableLinearLayout.setChecked(true);
    }
}
