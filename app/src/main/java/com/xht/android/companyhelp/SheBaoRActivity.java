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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SheBaoRActivity extends Activity {
    private static final String TAG = "SheBaoRActivity";
    private EditText editText1, editText2;
    private RadioGroup radioGroup;
    private Button jianIBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_she_bao_r);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setTextSize(18);
        mCustomView.setText("返回");
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
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

        Intent intent = getIntent();
        if (intent.getIntExtra("gors", 1) == 0) {
            radioGroup.check(R.id.radioR2);
        } else {
            radioGroup.check(R.id.radioR1);
        }
        editText1.setText(intent.getStringExtra("mingzi"));
        editText2.setText(intent.getStringExtra("idcard"));
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
