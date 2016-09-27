package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.provider.MyDatabaseManager;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 记账报税
 * Created by Administrator on 2016/7/30.
 */
public class DaiLiJIZhangActivity extends Activity implements RadioGroup.OnCheckedChangeListener{
    private static final String TAG = "DaiLiJiZhangActivity";

    private int mUId;
    private long mPhoneNum;
    private String mUserName;
    private ProgressDialog mProgDoal;
    private EditText mET;    //公司名称
    private TextView mHeJiTV;
    private EditText mPhone;
    private EditText mNameET;
    private int mMoney = 0;    //合计
    private int mArea = 0;    //spinner注册区域所选的id
    private int dljz = 3;	//代理记账-0,1,2,3,4,5,6
    private int nsrFlag = 0;	//纳税人默认为小规模
    private int zhouqiFlag = 3;	//默认为季度
    private int[] dljz6 = new int[7];
    private String[] dljz6JsonKey = new String[] {
            "Q1", "HY1", "Y1", "Q", "HY", "Y"};
    private boolean mShiFouFlag;	//是否加载价格完成

    private RadioGroup mNSRLRadioGroup; //纳税人类型
    private RadioGroup mJZZQRadioGroup; //记账周期

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jizhangbaoshui);
        Bundle bundle = getIntent().getBundleExtra("uData");
        mUId = bundle.getInt("uid", 0);
        mPhoneNum = bundle.getLong("uphone");
        mUserName = bundle.getString("uname");
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setTextSize(18);
        mCustomView.setText("下单预约-记账报税");
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        mNSRLRadioGroup = (RadioGroup) findViewById(R.id.rg1);
        mNSRLRadioGroup.setOnCheckedChangeListener(this);
        mJZZQRadioGroup = (RadioGroup) findViewById(R.id.rg2);
        mJZZQRadioGroup.setOnCheckedChangeListener(this);
        mET = (EditText) findViewById(R.id.cName_et);
        mPhone = (EditText) findViewById(R.id.book_phone);
        mNameET = (EditText) findViewById(R.id.name_jiazhang);
        mHeJiTV = (TextView) findViewById(R.id.shu_heji);
        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);    //注册区域的spinner
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.areas, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                LogHelper.i("spinner1-公司区域", spinner1.getSelectedItem().toString());
                mArea = position + 1;
                jiaZaiJaiGe();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        //下单预约
        Button yuYueBook = (Button) findViewById(R.id.bookYuQue);
        yuYueBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mET.getText() == null || mET.getText().toString().equals("")) {
                    App.getInstance().showToast(R.string.please_gongsiname);
                    return;
                }
                if (mPhone.getText() == null || mPhone.getText().toString().equals("")) {
                    App.getInstance().showToast(R.string.please_phone);
                    return;
                }
                if (mNameET.getText() == null || mNameET.getText().toString().equals("")) {
                    App.getInstance().showToast(R.string.please_name);
                    return;
                }
                fengzhuangJsonofPost();
            }
        });
        reFleshMoneyHeji();
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void createProgressDialog(String title) {
        if (mProgDoal == null) {
            mProgDoal = new ProgressDialog(this);
        }
        mProgDoal.setTitle(title);
        mProgDoal.setIndeterminate(true);
        mProgDoal.setCancelable(false);
        mProgDoal.show();
    }

    /**
     * 隐藏mProgressDialog
     */
    private void dismissProgressDialog()
    {
        if(mProgDoal != null)
        {
            mProgDoal.dismiss();
            mProgDoal = null;
        }
    }

    private void jiaZaiJaiGe() {
        mMoney = 0;
        createProgressDialog("价格初始化中...");
        VolleyHelpApi.getInstance().getJifGeofYeFu(1, mArea, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONObject jO = ((JSONObject) result).optJSONObject("entity");
                LogHelper.i("加载价格", jO.toString());
                for (int i = 0; i < 6; i++) {
                    dljz6[i + 1] = jO.optInt(dljz6JsonKey[i]);
                }
                //mMoney = jO.optInt("yewu1");

                mMoney = dljz6[dljz];

                mShiFouFlag = true;
                dismissProgressDialog();
                reFleshMoneyHeji();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
                finish();
            }
        });
    }

    /**
     * 获取用户所填的资料，封装成json
     */
    private void fengzhuangJsonofPost() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("area", mArea);
            jsonObj.put("CompName", mET.getText().toString());

            jsonObj.put("pNumber", mPhone.getText().toString());
            jsonObj.put("pName", mNameET.getText().toString());

            jsonObj.put("Money_HeJi", mMoney);
            jsonObj.put("dailijizhang", dljz);
            jsonObj.put("dailijizhang_money", dljz6[dljz]);
            jsonObj.put("userId", mUId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        createProgressDialog("订单提交中...");
        VolleyHelpApi.getInstance().postDingDanJiZhang(mUId, jsonObj, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i("订单提交成功", "2016-08-16");
                //用户姓名写入数据库
                if (mUserName == null || mUserName.isEmpty()) {
                    //用户姓名写入数据库
                    ContentValues cv = new ContentValues();
                    cv.put(MyDatabaseManager.MyDbColumns.NAME, mNameET.getText().toString());
                    String where = MyDatabaseManager.MyDbColumns.UID + " = ?";
                    String[] selectionArgs = {String.valueOf(mUId)};
                    DaiLiJIZhangActivity.this.getContentResolver().update(MyDatabaseManager.MyDbColumns.CONTENT_URI, cv, where, selectionArgs);
                    //数据有更新，更新一下内存的用户变量
                    Intent intent = new Intent(MyFragment.BRO_ACT_S);
                    intent.putExtra(MyFragment.UID_KEY, mUId);
                    intent.putExtra(MyFragment.PHONENUM_KEY, Long.parseLong("" + mPhoneNum));
                    intent.putExtra(MyFragment.UNAME_KEY, mNameET.getText().toString());
                    sendBroadcast(intent);
                }
                dismissProgressDialog();
                Bundle bundle = new Bundle();
                JSONObject tempJO = ((JSONObject) result).optJSONObject("entity");
                bundle.putString("shangpin", "记账报税");
                bundle.putString("bookListId", tempJO.optString("accountOrderId"));
                bundle.putFloat("pay_money", mMoney / 100.0f);
                Intent intent = new Intent(DaiLiJIZhangActivity.this, PayOptActivity.class);
                intent.putExtra("booklistdata", bundle);
                DaiLiJIZhangActivity.this.startActivity(intent);
                DaiLiJIZhangActivity.this.finish();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio1:
                zhouqiFlag = 1;
                break;
            case R.id.radio2:
                zhouqiFlag = 2;
                break;
            case R.id.radio3:
                zhouqiFlag = 3;
                break;
            case R.id.radioR1:
                nsrFlag = 3;
                break;
            case R.id.radioR2:
                nsrFlag = 0;
                break;
            default:
                break;
        }
        dljz = nsrFlag + zhouqiFlag;
        mMoney = dljz6[dljz];
        LogHelper.i("代理记账纳税人周期", "dljz=" + dljz);
    }

    /**
     * 更新合计的View
     */
    private void reFleshMoneyHeji() {
        LogHelper.i("更新合计的View", "mMoney = " + mMoney);
        mHeJiTV.setText(String.format(getResources().getString(R.string.heji_yuan), mMoney / 100.0f));
        mPhone.setText("" + mPhoneNum);
        if (mUserName != null) {
            mNameET.setText(mUserName);
            mNameET.setEnabled(false);
        }
    }
}
