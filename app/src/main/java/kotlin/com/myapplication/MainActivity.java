package kotlin.com.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kotlin.com.myapplication.activity.AdapterActivity;
import kotlin.com.myapplication.activity.TestBitmapActivity;
import kotlin.com.myapplication.adapter.BaseRecyclerAdapter;
import kotlin.com.myapplication.adapter.RecyclerViewHolder;
import kotlin.com.myapplication.donghua.DhActivity;

public class MainActivity extends AppCompatActivity {
    private final String CATEGORY = "com.zhi.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }
}
