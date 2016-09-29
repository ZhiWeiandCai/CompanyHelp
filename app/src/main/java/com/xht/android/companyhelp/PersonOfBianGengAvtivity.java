package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xht.android.companyhelp.util.LogHelper;

/**
 * Created by Administrator on 2016-8-26.
 * author: an
 */
public class PersonOfBianGengAvtivity extends Activity {
    private RadioGroup mRadioGroupGQ;
    private RadioGroup mRadioGroupZJ;
    private EditText mEDName;
    private EditText mEDSFZHao;
    private EditText mSFZAddress;
    private EditText mGuQuanBiLi;
    private Button mAddItemImg;
    private String textGuDGuQ =null;
    private String textZenJian=null;
    private int textGuQuanBiLi;
    private static final String TAG = "PersonOfBianGengAvtivity";
    private RadioButton mRadioButGD;
    private RadioButton mRadioButGQ;
    private RadioButton mRadioButZ;
    private RadioButton mRadioButJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_listview_biangeng);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("返回");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
        initView();
        Intent intent = getIntent();
        String mGuDongGuQuan = intent.getStringExtra("mGuDongGuQuan");
        String mGuQuanZenJian = intent.getStringExtra("mGuQuanZenJian");

        if (!TextUtils.isEmpty(mGuDongGuQuan)) {
            switch (mGuDongGuQuan) {
                case "变更股权":
                    mRadioButGQ.setChecked(true);
                    break;
                case "变更股东":
                    mRadioButGD.setChecked(true);
                    break;
            }
        }
        if (!TextUtils.isEmpty(mGuQuanZenJian)) {
            switch (mGuQuanZenJian) {
                case "减":
                    mRadioButJ.setChecked(true);
                    break;
                case "增":
                    mRadioButZ.setChecked(true);
                    break;
            }
        }
        mEDName.setText(intent.getStringExtra("mName"));
        mEDSFZHao.setText(intent.getStringExtra("mShengFengZheng"));
        mSFZAddress.setText(intent.getStringExtra("mSFZAddress"));
        mGuQuanBiLi.setText(intent.getStringExtra("mGuQuangBiLi"));
    }
    //初始化控件
    private void initView() {
        mRadioGroupGQ = (RadioGroup) findViewById(R.id.item_rg1);
        mRadioGroupZJ = (RadioGroup) findViewById(R.id.item_rg2);
        mRadioButGD = (RadioButton) findViewById(R.id.item_biangeng_gudong);
        mRadioButGQ = (RadioButton) findViewById(R.id.item_biangeng_guquan);

        mRadioButZ = (RadioButton) findViewById(R.id.item_biangeng_zeng);
        mRadioButJ = (RadioButton) findViewById(R.id.item_biangeng_jian);

        mEDName = (EditText) findViewById(R.id.item_biangeng_name);
        mEDSFZHao = (EditText) findViewById(R.id.item_shengfengzhenghao_edit);
        mSFZAddress = (EditText) findViewById(R.id.item_biangeng_sfz_address);
        mGuQuanBiLi = (EditText) findViewById(R.id.item_biangeng_guquan_bili);
        mAddItemImg = (Button) findViewById(R.id.item_imageview_contacts);

        mRadioGroupGQ.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.item_biangeng_gudong:
                        textGuDGuQ = "变更股东";
                        break;
                    case R.id.item_biangeng_guquan:
                        textGuDGuQ = "变更股权";
                        break;
                }
            }
        });
        mRadioGroupZJ.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.item_biangeng_zeng:
                        textZenJian = "增";
                        break;
                    case R.id.item_biangeng_jian:
                        textZenJian = "减";
                        break;
                }
            }
        });
        mAddItemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (TextUtils.isEmpty(textGuDGuQ)||TextUtils.isEmpty(textZenJian)){
                    App.getInstance().showToast("请完善信息后再保存！");
                    return;

                }
                if (mEDName.getText() == null || mEDName.getText().toString().isEmpty()) {
                    App.getInstance().showToast("请完善信息后再保存！");
                    return;
                }
                if (mEDSFZHao.getText() == null || mEDSFZHao.getText().toString().isEmpty()) {
                    App.getInstance().showToast("请完善信息后再保存！");
                    return;
                }
                if (mSFZAddress.getText() == null || mSFZAddress.getText().toString().isEmpty()) {
                    App.getInstance().showToast("请完善信息后再保存！");
                    return;
                }
                if (mGuQuanBiLi.getText() == null || mGuQuanBiLi.getText().toString().isEmpty()) {
                    App.getInstance().showToast("请完善信息后再保存！");
                    return;
                }

                String s = mGuQuanBiLi.getText().toString();
                if (Integer.parseInt(s)>100){
                    App.getInstance().showToast("股权比例不能超过100");
                    return;
                }
                //返回信息到listview
                Intent in = getIntent();
                int whichI = in.getIntExtra("whichItem", -1);
                LogHelper.i(TAG, "which=" + whichI);
                Bundle bundle = new Bundle();
                bundle.putString("bgGudongGuquan", textGuDGuQ);
                bundle.putString("zjGudongGuquan", textZenJian);
                bundle.putString("name", mEDName.getText().toString());
                bundle.putString("sfzHao", mEDSFZHao.getText().toString());
                bundle.putString("sfzAddress", mSFZAddress.getText().toString());
                bundle.putInt("guquanBili",Integer.parseInt(mGuQuanBiLi.getText().toString()) );
                bundle.putInt("mItem", whichI);
                in.putExtra("biangengServiceData", bundle);
                LogHelper.i(TAG, "返回的所有信息---------" + bundle.toString());
                setResult(RESULT_OK, in);
                finish();
            }
        });
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
