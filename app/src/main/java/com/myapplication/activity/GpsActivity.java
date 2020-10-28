package com.myapplication.activity;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.myapplication.R;
import com.myapplication.databinding.ActivityDataBindingBinding;
import com.myapplication.databinding.ActivityGps2Binding;
import com.myapplication.module.User;
import com.myapplication.module.UserHandler;

import java.text.DecimalFormat;

//继承BaseActivity会报错，还没解决
public class GpsActivity extends AppCompatActivity {
    Location location;
    ActivityGps2Binding binding;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =
                DataBindingUtil.setContentView(this, R.layout.activity_gps2);

        String serviceName = this.LOCATION_SERVICE;
        //获得位置服务的管理对象
        LocationManager locationManager = (LocationManager) getSystemService(serviceName);
        // 通过GPS获取定位的位置数据
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);// 高精度
        criteria.setAltitudeRequired(false);// 设置不需要获取海拔方向数据
        criteria.setBearingRequired(false);// 设置不需要获取方位数据
        criteria.setCostAllowed(true);// 设置允许产生资费
        criteria.setPowerRequirement(Criteria.POWER_HIGH);// 低功耗

        location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        updateToNewLocation(location);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 500, 1, new LocationListener() {//监听参数500ms更新一次或者1米更新一次
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

            public void onLocationChanged(Location location) {
                updateToNewLocation(location);//进入更新程序
            }
        });

    }

    private void updateToNewLocation(Location location) {

        if (location != null) {
            double latitude = location.getLatitude();//维度
            double longitude = location.getLongitude();//经度
            float speed = location.getSpeed();//取得速度
//            speed * 0.28;//米/秒转换成公里/小时
//            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//            String p = decimalFormat.format(speed * 3.6);//format 返回的是字符串
//            binding.gpsTxt.setText("速度：" + p);
        } else {
//            binding.gpsTxt.setText("无法获取地理信息");
        }

    }

}