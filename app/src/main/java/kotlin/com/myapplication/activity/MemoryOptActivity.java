package kotlin.com.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.SparseArray;

import java.util.Date;
import java.util.HashMap;

import kotlin.com.myapplication.R;

import static sun.security.krb5.Confounder.intValue;

/**
 * https://blog.csdn.net/wanderlustLee/article/details/84658976
 */
public class MemoryOptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_opt);
        //当容量到达0.75时，HashMap就会自动扩容浪费时间。所以初始化容量定为initialCapacity = (int) ((float) expectedSize / 0.75F + 1.0F)
        //HashMap 实际容量为大于capacity的2次幂

//        HashMap<String, Object> map = new HashMap<String, Object>();
//        for (int i = 0; i < 100000; i++) {
//            map.put(String.valueOf(i), new Date());//装入十万个对象查看占用内存，结果70.4M,没有初始化容量大小76.4《
//        }

//        ArrayMap<String, Object> map = new ArrayMap<String, Object>();
//        for (int i = 0; i < 100000; i++) {
//            map.put(String.valueOf(i), new Date());//装入十万个对象查看占用内存，结果57.4M.没有初始化容量大小65.7M
//        }
//        SparseArray<Object> map = new SparseArray<>();
//        for (int i = 0; i < 100000; i++) {
//            map.put(new Date());//装入十万个对象查看占用内存，结果70.4M,没有初始化容量大小76.4《
//        }
        //实验结果 1.当对象1W级别时三种存储接口实用内存基本一致，当十万级别时差距开始加大。
        //        2.初始化总是比未初始化，占用内存少
    }
}
