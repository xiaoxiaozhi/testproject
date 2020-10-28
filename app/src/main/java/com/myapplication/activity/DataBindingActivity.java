package com.myapplication.activity;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.databinding.library.baseAdapters.BR;
import com.myapplication.R;
import com.myapplication.databinding.ActivityDataBindingBinding;
import com.myapplication.module.ObservableUser;
import com.myapplication.module.User;
import com.myapplication.module.UserHandler;

public class DataBindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDataBindingBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        binding.setUserInfo(new User("张三", "123"));
        //单项绑定：1.User继承BaseObservable 2.控件绑定类属性android:text="@{userInfo.name}"
        //双向绑定：
        binding.setUserHandler(new UserHandler(binding.getUserInfo()));
        //实现了 Observable 接口的类允许注册一个监听器，当可观察对象的属性更改时就会通知这个监听器
        binding.getUserInfo().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.name) {

                }
                if (propertyId == BR.password) {

                }
            }
        });
        ///单项绑定：1.控件绑定类属性android:text="@{userInfo.name}"2.绑定专用类型ObservableField不用专门继承BaseObservable
        binding.setObsUser(new ObservableUser("李四", 100f, "123"));
//        ObservableUser observableUser = new ObservableUser("李四", 100f, "123");
        binding.getObsUser().name.set("李四哥");
    }
}