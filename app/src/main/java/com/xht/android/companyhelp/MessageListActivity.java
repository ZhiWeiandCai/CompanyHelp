package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.xht.android.companyhelp.util.LogHelper;

import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Administrator on 2016-9-8.
 */
public class MessageListActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.mess_list_title)//标题
    TextView mTextTitle;
    @InjectView(R.id.mess_list_content)//内容
    TextView mTextContent;
    @InjectView(R.id.mess_list_income)//收入
    TextView mTextIncome;
    @InjectView(R.id.mess_list_added_tax)//增值税
    TextView mTextTax;
    @InjectView(R.id.mess_list_development_tax)//文化建设税
    TextView mTextDeve;
    @InjectView(R.id.mess_list_company_tax)//企业所得税
    TextView mTextCompany;
    @InjectView(R.id.mess_list_declare_tax)//通用申报
    TextView mTextDeclare;
    @InjectView(R.id.mess_list_personal_tax)//个税
    TextView mTextPersonal;

    @InjectView(R.id.mess_list_notpass)//不通过
    Button mButNotPass;
    @InjectView(R.id.mess_list_pass)//通过
    Button mButPass;

    private static final String TAG = "MessageListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_mess_activity);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("确认税情-返回");
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
        ButterKnife.inject(this);
        mButNotPass.setOnClickListener(this);
        mButPass.setOnClickListener(this);
    }
    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCoder,KeyEvent event) {
        if (keyCoder == KeyEvent.KEYCODE_BACK) {
          //  mWebViewe.goBack();   //goBack()表示返回webView的上一页面
            finish();
            return true;
        }
        return false;
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
                LogHelper.i(TAG,"----"+value+"--");

            }
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mess_list_pass://通过

                break;
            case R.id.mess_list_notpass://不通过

                break;
        }
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