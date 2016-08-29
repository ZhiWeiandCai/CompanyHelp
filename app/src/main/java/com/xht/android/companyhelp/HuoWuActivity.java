package com.xht.android.companyhelp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xht.android.companyhelp.util.Arith;
import com.xht.android.companyhelp.util.LogHelper;

public class HuoWuActivity extends Activity {
    private static final String TAG = "HuoWuActivity";
    private EditText nameEt, guixingEt, danweiEt, shuliangEt, danjiaEt, jineEt;
    private Button jianIBtn;
    private int mShuL;  //数量
    private float mDanJ;    //单价
    private float mJE;  //金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huo_wu);
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
                        mJE = Arith.floatMul(mShuL, mDanJ);
                        jineEt.setText("" + mJE);
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
                    mDanJ = Float.parseFloat(s.toString());
                    if (shuliangEt.getText() != null && !shuliangEt.getText().toString().isEmpty()) {
                        mJE = Arith.floatMul(mShuL, mDanJ);
                        jineEt.setText("" + mJE);
                    }
                } else {
                    mDanJ = 0f;
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
        bundle.putFloat("hw5", mDanJ);
        bundle.putFloat("hw6", mJE);
        bundle.putInt("wItem", whichI);
        i.putExtra("huowuData", bundle);
        setResult(RESULT_OK, i);
        finish();
    }

}
