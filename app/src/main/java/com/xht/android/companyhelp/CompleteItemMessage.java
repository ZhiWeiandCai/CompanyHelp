package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.xht.android.companyhelp.util.LogHelper;

/**
 * Created by Administrator on 2016-8-30.
 */
public class CompleteItemMessage extends Activity{

    private EditText mEdName;
    private EditText mEdBiLi;
    private EditText mEdSFZPhone;
    private EditText mEdSFZAddress;
    private Button mButQueRen;
    private String mName;
    private String mBiLi;
    private String mSFZPhone;
    private String mSFZAddress;
    private String[] mWorks={"无职位","经理","监事","法人"};

    private int mWorkId=0;
    private Spinner mSPtext;
    private static final String TAG = "CompleteItemMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_item);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("完善信息-确认");
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
        //初始化
        initView();

        //确认填写正确
        mButQueRen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取数据
                initData();
                //确认按钮
                sureSendData();
            }
        });


        ArrayAdapter<CharSequence> mmItemWorkAdapter=new ArrayAdapter<CharSequence>(CompleteItemMessage.this,android.R.layout.simple_spinner_item,mWorks);
        mmItemWorkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSPtext.setAdapter(mmItemWorkAdapter);
        mSPtext.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mWorkId=position;
                LogHelper.i(TAG,"----id------"+mWorkId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void sureSendData() {
        if (TextUtils.isEmpty(mName)||TextUtils.isEmpty(mBiLi)||TextUtils.isEmpty(mSFZPhone)||TextUtils.isEmpty(mSFZAddress)){
            App.getInstance().showToast("请完善信息。。。");
            return;
        }
        Intent intent=getIntent();
        int whichI = intent.getIntExtra("ItemWhich", -1);

        intent.putExtra("ItemWhich",whichI);
        intent.putExtra("mName",mName);
        intent.putExtra("mBiLi",mBiLi);
        intent.putExtra("mSFZPhone",mSFZPhone);
        intent.putExtra("mSFZAddress",mSFZAddress);
        intent.putExtra("mZhiWei",mWorks[mWorkId]);
        setResult(RESULT_OK,intent);
        CompleteItemMessage.this.finish();
    }

    private void initData() {
        mName = mEdName.getText().toString().trim();
        mBiLi = mEdBiLi.getText().toString().trim();
        mSFZPhone = mEdSFZPhone.getText().toString().trim();
        mSFZAddress = mEdSFZAddress.getText().toString();
    }

    private void initView() {
        mEdName= (EditText) findViewById(R.id.item_2name);
        mEdBiLi= (EditText) findViewById(R.id.item_2bili);
        mEdSFZPhone= (EditText) findViewById(R.id.item_zhenjian_2number);
        mEdSFZAddress= (EditText) findViewById(R.id.item_zhenjian_2address);
        mButQueRen= (Button) findViewById(R.id.but_item_2addren);
        mSPtext= (Spinner) findViewById(R.id.item_sppiner_zhiwei);


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
