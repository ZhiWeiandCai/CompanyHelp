package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xht.android.companyhelp.util.LogHelper;

/**
 * Created by Administrator on 2016-9-27.
 */
public class ZhangHuMessage extends Activity implements View.OnClickListener {

    private int mUId;
    private Button tvZhangHu;
    private Button tvMiMa;

    private static final String TAG = "ZhangHuMessage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zh_message_activity);

        Bundle bundle = getIntent().getBundleExtra("mBundle");
        mUId = bundle.getInt("mUid",0);
        LogHelper.i(TAG,"---"+mUId);

        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("修改账户");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
        initView();




    }

    private void initView() {
        tvZhangHu = (Button) findViewById(R.id.zh_message);
        tvMiMa = (Button) findViewById(R.id.zh_xinmima);
        tvZhangHu.setOnClickListener(this);
        tvMiMa.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.zh_message:
                Intent intent=new Intent(ZhangHuMessage.this,ZhangHuManager.class);
                Bundle bundle = new Bundle();
                bundle.putInt("mUid", mUId);
                intent.putExtra("mBundle", bundle);
                startActivity(intent);
                break;
            case R.id.zh_xinmima:
                Intent intent1=new Intent(ZhangHuMessage.this,ZhangHuMiMa.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("mUid", mUId);
                intent1.putExtra("mBundle", bundle1);
                startActivity(intent1);
                break;

        }

    }
}
