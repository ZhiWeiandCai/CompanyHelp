package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FaPiaoActivity extends Activity {
    private static final String TAG = "FaPiaoActivity";

    RadioGroup rg;
    RadioButton mRBtn1, mRBtn2, mRBtn3;
    TextView mJinETV;
    Button mBookXiaDan;
    private Fragment mFragment1, mFragment2, mFragment3;
    private int mCurFragFlag;   //标记当前是哪一个Fragment
    private int mPrice1;
    private int mPrice2;
    private int mPrice3;
    private int[] mCompIds;
    private String[] mCompNames;
    private String[] mCompNSH;
    private String[] mCompPhone;
    private String[] mCompADD;
    private String[] mCompKHH;
    private String[] mCompKHCH;
    private int mUId;
    private ProgressDialog mProgDoal;
    private boolean mFlag;  //标记是否有公司可选

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getBundleExtra("uData");
        mUId = bundle.getInt("uid");
        setContentView(R.layout.activity_fa_piao);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("发票服务");
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
        rg = (RadioGroup) findViewById(R.id.switch_tabs);
        mRBtn1 = (RadioButton) findViewById(R.id.tab1);
        mRBtn2 = (RadioButton) findViewById(R.id.tab2);
        //mRBtn3 = (RadioButton) findViewById(R.id.tab3);
        mRBtn1.setTextColor(Color.BLUE);
        mRBtn2.setTextColor(Color.GRAY);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateFragmentVisibility();
                switch (checkedId) {
                    case R.id.tab1:
                        mRBtn1.setTextColor(Color.BLUE);
                        mRBtn2.setTextColor(Color.GRAY);
                        break;
                    case R.id.tab2:
                        mRBtn1.setTextColor(Color.GRAY);
                        mRBtn2.setTextColor(Color.BLUE);
                        break;
                    case R.id.tab3:
                        break;

                    default:
                        break;
                }
                refleshJiaGeView();
            }
        });
        mJinETV = (TextView) findViewById(R.id.shu_heji);
        mBookXiaDan = (Button) findViewById(R.id.bookYuQue);
        mBookXiaDan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject temp;
                Fragment fragment;
                switch (mCurFragFlag) {
                    case 1:
                        fragment = getFragmentManager().findFragmentByTag("f2");
                        if (fragment != null && fragment.isVisible()) {
                            temp = ((FaPiao2Fragment) fragment).postJsonData();
                            if (temp == null)
                                return;
                            try {
                                temp.put("ordContactId", mUId);
                                temp.put("orderFee", mPrice1);
                                postSJKPSQBookList(temp, mPrice1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case 2:
                        fragment = getFragmentManager().findFragmentByTag("f1");
                        if (fragment != null && fragment.isVisible()) {
                            temp = ((FaPiao1Fragment) fragment).postJsonData();
                            if (temp == null)
                                return;
                            try {
                                temp.put("userId", mUId);
                                temp.put("price", mPrice2);
                                postBookList(temp, mPrice2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case 3:
                        fragment = getFragmentManager().findFragmentByTag("f3");
                        if (fragment != null && fragment.isVisible()) {
                            temp = ((FaPiao3Fragment) fragment).postJsonData();
                            if (temp == null)
                                return;
                            try {
                                temp.put("ordContactId", mUId);
                                temp.put("orderFee", mPrice1);
                                postFPRZBookList(temp, mPrice1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }

            }
        });
    }

    // Update fragment visibility based on current check box state.
    void updateFragmentVisibility() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (mRBtn1.isChecked()) {
            mCurFragFlag = 1;
            ft.show(mFragment2);
        } else {
            ft.hide(mFragment2);
        }
        if (mRBtn2.isChecked()) {
            mCurFragFlag = 2;
            ft.show(mFragment1);
        } else {
            ft.hide(mFragment1);
        }
        /*if (mRBtn3.isChecked()) {
            mCurFragFlag = 3;
            ft.show(mFragment3);
        } else {
            ft.hide(mFragment3);
        }*/
        ft.commit();
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
        VolleyHelpApi.getInstance().getComListAndJiaGeofFP(uid, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONObject jiageJO;
                JSONArray companyJA;
                try {
                    jiageJO = ((JSONObject) result).getJSONObject("price");
                    mPrice1 = jiageJO.optInt("TaxInvoice");
                    mPrice2 = jiageJO.optInt("BusinessInvoice");
                    mPrice3 = jiageJO.optInt("VerifyInvoice");
                    companyJA = ((JSONObject) result).optJSONArray("companyName");
                    if (companyJA != null) {
                        int compJALength = companyJA.length();
                        mCompIds = new int[compJALength];
                        mCompNames = new String[compJALength];
                        mCompNSH = new String[compJALength];
                        mCompADD = new String[compJALength];
                        mCompPhone = new String[compJALength];
                        mCompKHH = new String[compJALength];
                        mCompKHCH = new String[compJALength];
                        for (int i = 0; i < compJALength; i++) {
                            JSONObject temp = companyJA.optJSONObject(i);
                            mCompIds[i] = temp.optInt("id");
                            mCompNames[i] = temp.optString("name");
                            mCompNSH[i] = temp.optString("comTaxeNo");
                            mCompADD[i] = temp.optString("comAddr");
                            mCompPhone[i] = temp.optString("comTel");
                            mCompKHH[i] = temp.optString("comBankName");
                            mCompKHCH[i] = temp.optString("comBankCode");
                        }
                        mFlag = true;
                    } else {
                        mFlag = false;
                        //2016年9月27日，原本是不论有没有公司可选，都可进入界面的。
                        // 现在先暂时对小后台客户开放
                        App.getInstance().showToast("对不起，该业务暂时只对小后台客户开放");
                        finish();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissProgressDialog();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                mFragment1 = fm.findFragmentByTag("f1");
                if (mFragment1 == null) {
                    if (mFlag) {
                        mFragment1 = FaPiao1Fragment.newInstance("", mCompIds, mCompNames, mCompNSH, mCompADD, mCompPhone, mCompKHH, mCompKHCH);
                    } else {
                        mFragment1 = FaPiao1Fragment.newInstance("", null, null, null, null, null, null, null);
                    }
                }
                ft.add(R.id.fragment_contain, mFragment1, "f1");
                mFragment2 = fm.findFragmentByTag("f2");
                if (mFragment2 == null) {
                    if (mFlag) {
                        mFragment2 = FaPiao2Fragment.newInstance("", mCompIds, mCompNames);
                    } else {
                        mFragment2 = FaPiao2Fragment.newInstance("", null, null);
                    }
                }
                ft.add(R.id.fragment_contain, mFragment2, "f2");
                /*mFragment3 = fm.findFragmentByTag("f3");
                if (mFragment3 == null) {
                    if (mFlag) {
                        mFragment3 = FaPiao3Fragment.newInstance("", mCompIds, mCompNames);
                    } else {
                        mFragment3 = FaPiao3Fragment.newInstance("", null, null);
                    }
                }
                ft.add(R.id.fragment_contain, mFragment3, "f3");*/
                ft.commit();
                updateFragmentVisibility();
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

    /**
     * 获取用户所填的资料,提交
     */
    private void postBookList(JSONObject jsonObj, final int price) {
        LogHelper.i("打印发票2的json--", jsonObj.toString());
        createProgressDialog("订单提交中...");
        VolleyHelpApi.getInstance().postDingDanFP2(mUId, jsonObj, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i("订单提交成功", "2016-08-25");
                dismissProgressDialog();
                Bundle bundle = new Bundle();
                JSONObject tempJO = ((JSONObject) result).optJSONObject("entity");
                bundle.putString("shangpin", "发票服务");
                bundle.putString("bookListId", tempJO.optString("orderId"));
                bundle.putFloat("pay_money", price / 100.0f);
                Intent intent = new Intent(FaPiaoActivity.this, PayOptActivity.class);
                intent.putExtra("booklistdata", bundle);
                FaPiaoActivity.this.startActivity(intent);
                FaPiaoActivity.this.finish();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });
    }

    /**
     * 税局开票的申请，提交
     * @param jsonObj
     * @param price
     */
    private void postSJKPSQBookList(JSONObject jsonObj, final int price) {
        createProgressDialog("订单提交中...");
        LogHelper.i("税局开票", jsonObj.toString());
        VolleyHelpApi.getInstance().postDingDanFP1(mUId, jsonObj, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i("订单提交成功", "2016-08-25");
                dismissProgressDialog();
                Bundle bundle = new Bundle();
                JSONObject tempJO = ((JSONObject) result).optJSONObject("entity");
                bundle.putString("shangpin", "发票服务");
                bundle.putString("bookListId", tempJO.optString("orderId"));
                bundle.putFloat("pay_money", price / 100.0f);
                Intent intent = new Intent(FaPiaoActivity.this, PayOptActivity.class);
                intent.putExtra("booklistdata", bundle);
                FaPiaoActivity.this.startActivity(intent);
                FaPiaoActivity.this.finish();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });
    }

    /**
     * 发票认证，提交
     * @param jsonObj
     * @param price
     */
    private void postFPRZBookList(JSONObject jsonObj, final int price) {
        createProgressDialog("订单提交中...");
        VolleyHelpApi.getInstance().postDingDanFP3(mUId, jsonObj, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i("订单提交成功", "2016-08-25");
                dismissProgressDialog();
                Bundle bundle = new Bundle();
                JSONObject tempJO = ((JSONObject) result).optJSONObject("entity");
                bundle.putString("shangpin", "发票服务");
                bundle.putString("bookListId", tempJO.optString("orderId"));
                bundle.putFloat("pay_money", price / 100.0f);
                Intent intent = new Intent(FaPiaoActivity.this, PayOptActivity.class);
                intent.putExtra("booklistdata", bundle);
                FaPiaoActivity.this.startActivity(intent);
                FaPiaoActivity.this.finish();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });
    }

    private void refleshJiaGeView() {
        if (mCurFragFlag == 1) {
            mJinETV.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice1 / 100.0f));
        } else if (mCurFragFlag == 2) {
            mJinETV.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice2 / 100.0f));
        } else if (mCurFragFlag == 3) {
            mJinETV.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice3 / 100.0f));
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

    /**
     * 2016-10-08
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                LogHelper.i(TAG, "成功接收到数据");
                Fragment fragment = getFragmentManager().findFragmentByTag("f1");
                if (fragment != null && fragment.isVisible()) {
                    fragment.onActivityResult(requestCode, resultCode, intent);
                }
            }
        }
    }
}
