package com.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.myapplication.R;

public class HybridActivity extends BaseActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hybrid);
        webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/test.html");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url); //该界面跳转的 url仍由该界面截获
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);//允许activity使用该HTML的js方法
    }

}
