package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xht.android.companyhelp.util.Arith;
import com.xht.android.companyhelp.util.LogHelper;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class HuoWuActivity extends Activity {
    private static final String TAG = "HuoWuActivity";
    private EditText nameEt, guixingEt, danweiEt, shuliangEt, danjiaEt, jineEt;
    private Button jianIBtn;
    private int mShuL;  //数量
    private double mDanJ;    //单价
    private double mJE;  //金额

    DecimalFormat df = new DecimalFormat("0.00");//保留两位小数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huo_wu);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setTextSize(18);
        mCustomView.setText("返回");
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
        nameEt = (EditText) findViewById(R.id.i_hwName_et);
        guixingEt = (EditText) findViewById(R.id.guigexinghao_et);
        danweiEt = (EditText) findViewById(R.id.danwei_et);
        shuliangEt = (EditText) findViewById(R.id.shuliang_et);
        danjiaEt = (EditText) findViewById(R.id.danjia_et);
        jineEt = (EditText) findViewById(R.id.je_buhanshui_et);
        shuliangEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogHelper.i(TAG, "str=" + s.toString());

                if (!s.toString().isEmpty()) {
                    mShuL = Integer.parseInt(s.toString());
                    if (danjiaEt.getText() != null && !danjiaEt.getText().toString().isEmpty()) {
                        mJE = Arith.round(Arith.mul2(mShuL, mDanJ), 2);
                        BigDecimal bd = new BigDecimal(mJE);
                        jineEt.setText(df.format(bd));
                    }
                } else {
                    mShuL = 0;
                    mJE = 0;
                    jineEt.setText("");
                }

            }
        });
        danjiaEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogHelper.i(TAG, "str=" + s.toString());

                if (!s.toString().isEmpty()) {
                    mDanJ = Double.parseDouble(s.toString());
                    if (shuliangEt.getText() != null && !shuliangEt.getText().toString().isEmpty()) {
                        mJE = Arith.round(Arith.mul2(mShuL, mDanJ), 2);
                        BigDecimal bd = new BigDecimal(mJE);
                        jineEt.setText(df.format(bd));
                    }
                } else {
                    mDanJ = 0d;
                    mJE = 0;
                    jineEt.setText("");
                }

            }
        });
        jianIBtn = (Button) findViewById(R.id.jian_huowu);
        jianIBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkInfoComp();
            }
        });
        Intent intent = getIntent();
        nameEt.setText(intent.getStringExtra("hwname"));
        guixingEt.setText(intent.getStringExtra("guige"));
        danweiEt.setText(intent.getStringExtra("danwei"));
        shuliangEt.setText("" + intent.getIntExtra("shul", 0));
        mDanJ = intent.getDoubleExtra("danjia", 0d);
        mJE = intent.getDoubleExtra("jine", 0d);
        BigDecimal bd = new BigDecimal(mDanJ);
        danjiaEt.setText(df.format(bd));
        bd = new BigDecimal(mJE);
        jineEt.setText(df.format(bd));
    }

    private void checkInfoComp() {
        if (nameEt.getText() == null || nameEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请完善信息后再保存！");
            return;
        }
        if (guixingEt.getText() == null || guixingEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请完善信息后再保存！");
            return;
        }
        if (danweiEt.getText() == null || danweiEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请完善信息后再保存！");
            return;
        }
        if (shuliangEt.getText() == null || shuliangEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请完善信息后再保存！");
            return;
        }
        if (danjiaEt.getText() == null || danjiaEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请完善信息后再保存！");
            return;
        }

        Intent i = getIntent();
        int whichI = i.getIntExtra("whichItem", -1);
        Bundle bundle = new Bundle();
        bundle.putString("hw1", nameEt.getText().toString());
        bundle.putString("hw2", guixingEt.getText().toString());
        bundle.putString("hw3", danweiEt.getText().toString());
        bundle.putInt("hw4", mShuL);
        bundle.putDouble("hw5", mDanJ);
        bundle.putDouble("hw6", mJE);
        bundle.putInt("wItem", whichI);
        i.putExtra("huowuData", bundle);
        setResult(RESULT_OK, i);
        finish();
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
