package com.crucentralcoast.app.presentation.views.webview;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseAppCompatActivity
{

    @BindView(R.id.web_view) WebView webView;

    public static final String EXTRA_URL = "EXTRA_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        unbinder = ButterKnife.bind(this);
        String url = getIntent().getStringExtra(EXTRA_URL);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
        setupActionBar(url);
    }

    private void setupActionBar(String url) {
        setTitle(url);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }
}