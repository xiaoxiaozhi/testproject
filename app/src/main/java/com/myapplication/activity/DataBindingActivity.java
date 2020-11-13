package com.myapplication.activity;

;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;


import com.myapplication.R;
import com.myapplication.databinding.ActivityDataBindingBinding;
import com.myapplication.module.ObservableUser;
import com.myapplication.module.User;
import com.myapplication.module.UserHandler;

//1.单向绑定
//2.双向绑定 比单向绑定多了个 =号 android:text="@={goods.name}" ,
//3.子activity使用DataBinding
//4.自定义控件实现DataBinding
//要点：android:text="@{obsUser.name??myList.get(1)}" 空合并运算符 ?? 会取第一个不为 null 的值作为返回值
//5.BindingAdapter
//6.BindingConversion
public class DataBindingActivity extends BaseActivity {
    private final static String TAG = "DataBindingActivity";

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

    /**
     *
     */
    @BindingAdapter({"url"})
    public static void loadImage(ImageView view, String url) {
        Log.e(TAG, "loadImage url : " + url);
    }

    /**
     * dataBInding框架提供一种介入方式，任何控件在 xml中使用了 android:text属性，都可以在这里介入。
     *
     * @param view
     * @param text
     */
    @BindingAdapter("android:text")
    public static void setText(TextView view, CharSequence text) {
        Log.e(TAG, "TextView " + view.hashCode() + " CharSequence : " + text);
        final CharSequence oldText = view.getText();
        if (text == oldText || (text == null && oldText.length() == 0)) {
            return;
        }
        if (text instanceof Spanned) {
            if (text.equals(oldText)) {
                return; // No change in the spans, so don't set anything.
            }
        }
        view.setText(text);
    }
}