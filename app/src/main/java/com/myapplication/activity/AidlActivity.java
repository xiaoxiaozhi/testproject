package com.myapplication.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.myapplication.IMyAidlInterface;
import com.myapplication.R;
import com.myapplication.aidlservice.RemoteService;

public class AidlActivity extends AppCompatActivity {
    IMyAidlInterface iMyAidlInterface;
    MyConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        connection = new MyConnection();
        findViewById(R.id.press).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("com.myapplication.aidl");
//                intent.setPackage("com.myapplication");
                Intent intent = new Intent(AidlActivity.this, RemoteService.class);
//                intent.setPackage("com.myapplication");
                bindService(intent, connection, BIND_AUTO_CREATE);
            }
        });
    }

    class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                Log.i(AidlActivity.class.getSimpleName(), iMyAidlInterface.testAidl("启用AIDL"));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
