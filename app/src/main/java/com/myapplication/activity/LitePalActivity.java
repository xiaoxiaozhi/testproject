package com.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myapplication.R;
import com.myapplication.module.SettingEntry;

import org.litepal.crud.DataSupport;

import java.util.Random;

public class LitePalActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lite_pal);
        textView = findViewById(R.id.typeName);
        Button button = findViewById(R.id.change);
        button.setOnClickListener(this);
        SettingEntry entry = DataSupport.where("type=?", "2").findFirst(SettingEntry.class);
        if (entry == null) {
            entry = new SettingEntry("疲劳驾驶", 30, 0.8f, 5, 4, 2);
            entry.save();
        }
        textView.setText(entry.getTypeName());
    }

    @Override
    public void onClick(View v) {
        SettingEntry entry = DataSupport.where("type=?", "2").findFirst(SettingEntry.class);
        if (entry != null) {
            entry.setTypeName("疲劳驾驶" + new Random().nextInt(100));
            textView.setText(entry.getTypeName());
            entry.save();
        }
    }
}