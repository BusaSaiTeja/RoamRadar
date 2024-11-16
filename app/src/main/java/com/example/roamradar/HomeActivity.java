package com.example.roamradar;

import android.os.Bundle;
import android.webkit.WebView;

public class HomeActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }
}
