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
import android.widget.Spinner;
import android.widget.TextView;

import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.provider.MyDatabaseManager;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegiTrademaskActivity extends Activity {
    private static final String TAG = "RegiTrademaskActivity";
    private int mUId;
    private long mUPhone;
    private String mUName;
    private Spinner mSpinner;
    private EditText mCompNameET, mRelaPersonNameET, mRelaPersonPhoneET, mTmNameET;
    TextView mJinETV;
    Button mBookXiaDan;
    private ProgressDialog mProgDoal;

    private int mPrice;
    private int[] mCompIds;
    private String[] mCompNames;
    private boolean mFlag;  //标记是否有公司可选
    private int mPosiFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getBundleExtra("uData");
        mUId = bundle.getInt("uid");
        mUPhone = bundle.getLong("uphone");
        mUName = bundle.getString("uname");
        setContentView(R.layout.activity_regi_trademask);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("注册商标");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
        initView();
        getComListAndJiaGeOfFP(mUId);
    }

    private void initView() {
        mCompNameET = (EditText) findViewById(R.id.jia_c_et_x);
        mRelaPersonNameET = (EditText) findViewById(R.id.relativePersonET);
        mRelaPersonPhoneET = (EditText) findViewById(R.id.relativePersonPET);
        mTmNameET = (EditText) findViewById(R.id.trademaskName);
        mJinETV = (TextView) findViewById(R.id.shu_heji);
        mBookXiaDan = (Button) findViewById(R.id.bookYuQue);
        mBookXiaDan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfoCompAndPost();
            }
        });
        mSpinner = (Spinner) findViewById(R.id.jia_c_et);
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

    /**
     * 根据用户id获取公司列表和发票价格
     */
    private void getComListAndJiaGeOfFP(int uid) {
        createProgressDialog("价格获取中...");
        VolleyHelpApi.getInstance().getComListAndJGOfRegiT(uid, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONObject jiageJO;
                JSONArray companyJA;
                try {
                    jiageJO = ((JSONObject) result).getJSONObject("price");
                    mPrice = jiageJO.optInt("RegTrademark");

                    companyJA = ((JSONObject) result).optJSONArray("companyName");
                    if (companyJA != null) {
                        int compJALength = companyJA.length();
                        mCompIds = new int[compJALength];
                        mCompNames = new String[compJALength];

                        for (int i = 0; i < compJALength; i++) {
                            JSONObject temp = companyJA.optJSONObject(i);
                            mCompIds[i] = temp.optInt("id");
                            mCompNames[i] = temp.optString("name");

                        }
                        mFlag = true;
                    } else {
                        mFlag = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissProgressDialog();
                refleshJiaGeView();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
                finish();
            }
        });
    }

    private void refleshJiaGeView() {
        mJinETV.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice / 100.0f));
        ArrayAdapter<CharSequence> arrayAdapter;
        if (mFlag) {
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mCompNames);
        } else {
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[]{"没有可选公司"});
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                LogHelper.i("spinner1-公司区域", mSpinner.getSelectedItem().toString());
                mPosiFlag = position;
                if (mFlag)
                    mCompNameET.setText(mSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        if (!mFlag)
            mSpinner.setVisibility(View.GONE);
        else
            mCompNameET.setText(mCompNames[mPosiFlag]);
        mRelaPersonPhoneET.setText("" + mUPhone);
        if (mUName != null && !mUName.isEmpty()) {
            mRelaPersonNameET.setText(mUName);
            mRelaPersonNameET.setEnabled(false);
        }
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

    private void checkInfoCompAndPost() {
        if (mCompNameET.getText() == null || mCompNameET.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return;
        }
        if (mRelaPersonNameET.getText() == null || mRelaPersonNameET.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return;
        }
        if (mRelaPersonPhoneET.getText() == null || mRelaPersonPhoneET.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return;
        }
        if (mTmNameET.getText() == null || mTmNameET.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return;
        }
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("UserId", mUId);
            jObject.put("CompanyName", mCompNameET.getText().toString());
            jObject.put("RelaPersonName", mRelaPersonNameET.getText().toString());
            jObject.put("RelaPersonPNum", mRelaPersonPhoneET.getText().toString());
            jObject.put("TradeMaskName", mTmNameET.getText().toString());
            jObject.put("price", mPrice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogHelper.i(TAG, jObject.toString());
        createProgressDialog("订单提交中...");
        VolleyHelpApi.getInstance().postDDRegiTm(mUId, jObject, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i("订单提交成功", "2016-08-25");
                if (mUName == null || mUName.isEmpty()) {
                    //用户姓名写入数据库
                    ContentValues cv = new ContentValues();
                    cv.put(MyDatabaseManager.MyDbColumns.NAME, mRelaPersonNameET.getText().toString());
                    String where = MyDatabaseManager.MyDbColumns.UID + " = ?";
                    String[] selectionArgs = {String.valueOf(mUId)};
                    RegiTrademaskActivity.this.getContentResolver().update(MyDatabaseManager.MyDbColumns.CONTENT_URI, cv, where, selectionArgs);
                    //数据有更新，更新一下内存的用户变量
                    Intent intent = new Intent(MyFragment.BRO_ACT_S);
                    intent.putExtra(MyFragment.UID_KEY, mUId);
                    intent.putExtra(MyFragment.PHONENUM_KEY, Long.parseLong(mRelaPersonPhoneET.getText().toString()));
                    intent.putExtra(MyFragment.UNAME_KEY, mRelaPersonNameET.getText().toString());
                    sendBroadcast(intent);
                }
                dismissProgressDialog();
                Bundle bundle = new Bundle();
                JSONObject tempJO = ((JSONObject) result).optJSONObject("entity");
                bundle.putString("shangpin", "注册商标");
                bundle.putString("bookListId", tempJO.optString("orderId"));
                bundle.putFloat("pay_money", mPrice / 100.0f);
                Intent intent = new Intent(RegiTrademaskActivity.this, PayOptActivity.class);
                intent.putExtra("booklistdata", bundle);
                RegiTrademaskActivity.this.startActivity(intent);
                RegiTrademaskActivity.this.finish();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });
    }
}
