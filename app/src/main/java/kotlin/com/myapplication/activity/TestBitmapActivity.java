package kotlin.com.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;

import com.mylibrary.utils.MaxTextLengthFilter;
import com.mylibrary.utils.MyInputFilter;

import java.util.Random;

import kotlin.com.myapplication.R;

public class TestBitmapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bitmap);
        EditText et = findViewById(R.id.text);
        et.setFilters(new InputFilter[]{new MaxTextLengthFilter(et, 3)});
//        for (int i = 0; i < 20; i++) {
//            Log.i("随机数", String.valueOf(new Random().nextInt(5)));
//        }
    }
}
