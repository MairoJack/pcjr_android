package com.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pcjr.R;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * WebView
 * Created by Mario on 2016/5/24.
 */
public class WebViewActivity extends Activity {
    private PtrClassicFrameLayout mPtrFrame;
    private RelativeLayout back;
    private WebView webView;
    private ProgressDialog dialog;
    private TextView title;
    private String url;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        initView();
        initData();
    }

    public void initView() {
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_frame);
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        webView = (WebView) findViewById(R.id.web_view);
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("正在加载...");
        dialog.show();

        //下拉刷新
        mPtrFrame.disableWhenHorizontalMove(true);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, webView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                webView.loadUrl(url);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings
                .setUserAgentString(webSettings.getUserAgentString() + "NewpcjrApp");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();
                mPtrFrame.refreshComplete();
                webView.loadUrl("javascript:appdeletehead()");
            }
        });
        url = intent.getStringExtra("url");
        title.setText(intent.getStringExtra("title"));
        webView.loadUrl(url);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;

    }

}
