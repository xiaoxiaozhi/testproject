package com.myapplication;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ln.permission.PermissionsChecker;
import com.ln.permission.activity.PermissionsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.myapplication.activity.AdapterActivity;
import com.myapplication.activity.BaseActivity;
import com.myapplication.activity.MemeryActivity;
import com.myapplication.activity.TestBitmapActivity;
import com.myapplication.adapter.BaseRecyclerAdapter;
import com.myapplication.adapter.RecyclerViewHolder;
import com.myapplication.donghua.DhActivity;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends BaseActivity {
    private final String CATEGORY = "com.zhi.app";
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };
    private static final int REQUEST_CODE = 0; // 请求码
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    public static MemeryActivity ma;
    static {
        System.loadLibrary("native-lib");
    }
    @Override
    public void initToolbar(@NotNull Toolbar toolbar) {
        toolbar.setTitle("日常记录");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().getName(), "onCreate");
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycle);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//最基本属性，不设置不显示
        BaseRecyclerAdapter<String> adapter = new BaseRecyclerAdapter<String>(this) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.main_item;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, String item) {
                holder.getTextView(R.id.name).setText(item);
            }
        };

        ArrayList<String> result = new ArrayList<String>();
        Intent intent1 = new Intent(Intent.ACTION_MAIN, null);
        intent1.addCategory(CATEGORY);
        HashMap<String, Intent> map = new HashMap<>();
        Long start = System.currentTimeMillis();// new Date().getTime()其实引用了该函数
        for (ResolveInfo info : getPackageManager().queryIntentActivities(intent1, 0)) {
            map.put((String) info.loadLabel(getPackageManager()),
                    new Intent().setClassName(info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name));
            Log.i("获取activity", info.activityInfo.name);
        }
        Log.i("耗时", System.currentTimeMillis() - start + "毫秒");
        adapter.addAll(new ArrayList<String>(map.keySet()));
        adapter.setOnItemClickListener((View itemView, int pos) -> {
            startActivity(map.get(adapter.getItem(pos)));
        });
        recyclerView.setAdapter(adapter);
        //搜索忽略电池优化，才能发现这个设置
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isIgnoringBatteryOptimizations()) {
//            requestIgnoreBatteryOptimizations();
//        }
        //自启管理 里面有后台活动项。为了保活必须同时开启 忽略电池优化、后台活动运行。
//        if (isHuawei()) {
//            goHuaweiSetting();
//        }
        // 如参考，最好加入一个界面开启这些服务
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mPermissionsChecker = new PermissionsChecker(this);
//        // 缺少权限时, 进入权限配置页面
//        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
//            startPermissionsActivity();
//        } else {
//        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            //拒绝
            finish();
        } else if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
            //同意
        }
    }

    /**
     * 根据这个方法判断应用是否在保活名单中
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return isIgnoring;
    }

    /**
     * 加入保活白名单
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isHuawei() {
        if (Build.BRAND == null) {
            return false;
        } else {
            return Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor");
        }
    }

    private void goHuaweiSetting() {
        try {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } catch (Exception e) {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
        }
    }

    /**
     * 跳转到指定应用的首页
     */
    private void showActivity(@NonNull String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     */
    private void showActivity(@NonNull String packageName, @NonNull String activityDir) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
