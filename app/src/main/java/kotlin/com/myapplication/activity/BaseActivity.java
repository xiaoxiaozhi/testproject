package kotlin.com.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kotlin.com.myapplication.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
