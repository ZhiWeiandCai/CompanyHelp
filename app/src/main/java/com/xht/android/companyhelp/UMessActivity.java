package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xht.android.companyhelp.util.LogHelper;

import java.util.Set;

/**
 * Created by Administrator on 2016-9-7.
 * author: an
 * 展示通知消息页面
 */
public class UMessActivity extends Activity {
    private static final String TAG = "UMessActivity";
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.umess_activity);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("返回");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        mWebView = (WebView) findViewById(R.id.web_view);
       Intent intent=getIntent();
        Bundle mBundle = intent.getBundleExtra("listMess");
        if (mBundle!=null){
            String url=mBundle.getString("url");

            mWebView.loadUrl(url);

        }
        //mWebView.loadUrl("http://blog.csdn.net/abc5382334/article/details/23934101");
        WebSettings webSettings = mWebView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
       // webSettings.setAllowFileAccess(true);
        //设置支持缩放
        //webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        mWebView.setWebViewClient(new WebViewClient() {
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Bundle bun = getIntent().getExtras();
        if (bun != null)
        {
            Set<String> keySet = bun.keySet();
            for (String key : keySet) {
                String value = bun.getString(key);
                mWebView.loadUrl(value);
                LogHelper.i(TAG,"----"+value+"--");
            }
        }
    }
    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCoder,KeyEvent event) {
        if (mWebView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
            mWebView.goBack();   //goBack()表示返回webView的上一页面
            return true;
        }
        finish();
        return false;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    }
