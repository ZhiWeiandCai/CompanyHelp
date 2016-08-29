package com.xht.android.companyhelp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class SheBaoRActivity extends Activity {
    private static final String TAG = "SheBaoRActivity";
    private EditText editText1, editText2;
    private RadioGroup radioGroup;
    private Button jianIBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_she_bao_r);
        radioGroup = (RadioGroup) findViewById(R.id.rg1);
        editText1 = (EditText) findViewById(R.id.shebaoren_name);
        editText2 = (EditText) findViewById(R.id.shebaoren_idcard);

        jianIBtn = (Button) findViewById(R.id.jian_item_btn);
        jianIBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkInfoComp();
            }
        });
    }

    private void checkInfoComp() {
        if (editText1.getText() == null || editText1.getText().toString().isEmpty()) {
            App.getInstance().showToast("请完善信息后再保存！");
            return;
        }
        if (editText2.getText() == null || editText2.getText().toString().isEmpty()) {
            App.getInstance().showToast("请完善信息后再保存！");
            return;
        }
        Intent i = getIntent();
        int whichI = i.getIntExtra("whichItem", -1);
        Bundle bundle = new Bundle();
        if (radioGroup.getCheckedRadioButtonId() == R.id.radioR1) {
            bundle.putInt("sbr1", 1);
        } else {
            bundle.putInt("sbr1", 0);
        }

        bundle.putString("sbr2", editText1.getText().toString());
        bundle.putString("sbr3", editText2.getText().toString());
        bundle.putInt("wItem", whichI);
        i.putExtra("shebaorData", bundle);
        setResult(RESULT_OK, i);
        finish();
    }
}
