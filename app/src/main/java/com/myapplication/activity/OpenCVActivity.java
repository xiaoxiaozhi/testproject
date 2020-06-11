package com.myapplication.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.myapplication.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class OpenCVActivity extends AppCompatActivity {

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_c_v);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        final Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
        imageView.setImageBitmap(bitmap);

        final Button button2 = (Button)findViewById(R.id.button);
        button2.setText("转换");
        button2.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                i++;
                Mat rgbMat = new Mat();
                Mat grayMat = new Mat();
                Utils.bitmapToMat(bitmap, rgbMat);
                Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);
                Bitmap grayBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
                Utils.matToBitmap(grayMat, grayBmp);
                ImageView imageView = (ImageView)findViewById(R.id.imageView);
                if(i % 2 == 1)
                    imageView.setImageBitmap(grayBmp);
                else
                    imageView.setImageBitmap(bitmap);
            }
        });
    }
}
