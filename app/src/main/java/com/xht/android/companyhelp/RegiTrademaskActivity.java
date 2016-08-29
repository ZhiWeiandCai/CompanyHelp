package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegiTrademaskActivity extends Activity {
    private static final String TAG = "RegiTrademaskActivity";
    private int mUId;
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
        setContentView(R.layout.activity_regi_trademask);
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

    /**
     * 根据用户id获取公司列表和发票价格
     */
    private void getComListAndJiaGeOfFP(int uid) {
        createProgressDialog("价格获取中...");
        VolleyHelpApi.getInstance().getComListAndJiaGeofFP(uid, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONObject jiageJO;
                JSONArray companyJA;
                try {
                    jiageJO = ((JSONObject) result).getJSONObject("price");
                    mPrice = jiageJO.optInt("TaxInvoice");

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

    }
}
