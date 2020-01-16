package com.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mylibrary.test.MyAnnotation;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.myapplication.R;
import test.com.myprocess.MyButterknife;

// 跟java 一样 执行顺序 静态代码块、代码块、构造函数、onCreate 在onCreate执行之前Toast执行不了
public class Testctivity extends AppCompatActivity {
    {
        Log.i(getClass().getSimpleName(), "代码块");
    }

    static {
        Log.i("Testctivity", "静态代码块");
    }

    public Testctivity() {
        Log.i(getClass().getSimpleName(), "构造函数");
//        Toast.makeText(getApplication(), "toast", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testctivity);
        Log.i(getClass().getName(), "onCreate");
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                synchronized ("test") {
//                    System.out.println("notify前");
//                    "test".notify();
//                    System.out.println("notify后");
//                }
//            }
//        }.start();
//        synchronized ("test") {
//            try {
//                System.out.println("wait前");
//                "test".wait();
//                System.out.println("wait后");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("执行结束");
        ProducerConsumer pConsumer = new ProducerConsumer();
        new Thread("生产者") {
            public void run() {
                while (true) {
                    pConsumer.product();
                }
            }
        }.start();
        new Thread("消费者") {
            public void run() {
                while (true) {
                    pConsumer.consumer();
                }
            }
        }.start();
    }

    public class ProducerConsumer {
        private ReentrantLock reentrantLock = new ReentrantLock();
        private Condition condition = reentrantLock.newCondition();
        private ArrayList<String> listStr = new ArrayList<String>();

        void product() {
            try {
                reentrantLock.lock();
                while (listStr.size() != 0) {
                    condition.wait();
                }
                listStr.add("1");
                System.out.println(
                        Thread.currentThread().getName() + "_" + System.currentTimeMillis() + "_" + listStr.size());
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }

        }

        void consumer() {
            try {
                reentrantLock.lock();
                while (listStr.size() == 0) {
                    condition.wait();
                }
                listStr.remove(0);
                System.out.println(
                        Thread.currentThread().getName() + "_" + System.currentTimeMillis() + "_" + listStr.size());
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }

        }
    }
}
