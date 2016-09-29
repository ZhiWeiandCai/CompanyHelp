package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.umeng.message.ALIAS_TYPE;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/20.
 * 服务里面的注册资金变更的活动
 *
 *  author: an
 */
public class ZhuCeZiJinActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    private EditText mChangeCompany;//输入公司名称
    private RadioGroup mRadioGroup;//ViewGroid

    private EditText mEdTotalMoney;//输入总金额
    private TextView mTVTotalMoney;//获得下单金额
    private Button mSendBooks; //提交订单
    private Spinner mSpinner;
    private ProgressDialog mProgDoal;//对话框

    private int currentRadio=1 ;
    private int mUId;//下单用户id
    private int mPrice;

    private String[] mCompNames;//公司名字数组
    private int[] mCompIds;
    private int mSelectedCompId;//选中公司的ID
    private String companyName;//输入或选择的公司名字

    private static final String TAG = "ZhuCeZiJinActivity";

   private static final String XHT="XHT";//String SINA_WEIBO = "SINA_WEIBO";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhucezijin);
        Bundle bundle = getIntent().getBundleExtra("uData");
        mUId = bundle.getInt("uid");//下单人ID

      //  String uid=String.valueOf(mUId);
       /* //设置用户ID为标签
                try {
                    //App.getmPushAgent().getTagManager().add("movie","sport");
                    App.getmPushAgent().getTagManager().add("uid");
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

        //设置用户id为标签  TODO 在这个界面尝试的设置标签
                try {
                    App.getmPushAgent().getTagManager().add(mUId+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }



        //特定用户
       // App.getmPushAgent().setAlias("uid", ALIAS_TYPE.SINA_WEIBO);
       // App.getmPushAgent().setExclusiveAlias("uid", ALIAS_TYPE.SINA_WEIBO);

        LogHelper.i(TAG,"用户ID---"+mUId);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("下单预约-变更注册资金");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);


        mChangeCompany = (EditText) findViewById(R.id.change_company_name);
        mRadioGroup = (RadioGroup) findViewById(R.id.change_radio);
        mEdTotalMoney = (EditText) findViewById(R.id.change_input_total_money);
        mTVTotalMoney = (TextView) findViewById(R.id.change_dingdan_money);
        mSendBooks = (Button) findViewById(R.id.change_seng_dingdan);
        mSpinner = (Spinner) findViewById(R.id.select_spinner);


        mChangeCompany.setEnabled(false);
        LogHelper.i(TAG,"注册公司变更资金---------------");
        //获取公司列表
        getComListZhuCe(mUId);
        initView();//初始化控件
    }

    /**
     * 初始化控件
     */
    private void initView() {

        //获取公司客户名称
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogHelper.i("单击了某些选项", "position=" + position + "--gongsi=" + mCompNames[position]);
                mSelectedCompId = mCompIds[position];
                LogHelper.i(TAG, mCompNames[position]);
                LogHelper.i(TAG,mSelectedCompId+"----");
                mChangeCompany.setText(mCompNames[position].toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //RadioButton设置选中那个
        mRadioGroup.setOnCheckedChangeListener(this);


        //提交订单的点击事件
        mSendBooks.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                companyName = mChangeCompany.getText().toString();

                //获取总金额
                String totalMoney = mEdTotalMoney.getText().toString();
                if (TextUtils.isEmpty(totalMoney)) {
                    App.getInstance().showToast("请输入变更后的总资金");
                    LogHelper.i(TAG, "请输入变更后的总资金");
                    return;
                }
                //封装数据成JSON对象提交到服务器数据库
                sendChangeMoneyJSONPost();

            }
        });

    }

    /**
     * 获取用户变更之后的信息封装成JSONObject
     */
    //如果不是我们公司客户，那公司名提供录入，

    public void sendChangeMoneyJSONPost() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("TotalMoney", mEdTotalMoney.getText().toString());//变更注册资金总额
            obj.put("BiangengFangshi", currentRadio);//1为增资，0为减资
            obj.put("CompanyName", companyName);
            obj.put("CustomerId",mUId);//下单用户id
           // obj.put("CompanyId",mSelectedCompId);//公司id
            obj.put("Price",mPrice);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        createProgressDialog("订单提交中...");
        LogHelper.i(TAG, "----提交的所有信息" + obj.toString());

        createProgressDialog("订单提交中...");
        VolleyHelpApi.getInstance().postDingDanBianGeng(mUId, obj, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i("订单提交成功", "2016-08-23");
                dismissProgressDialog();
                Bundle bundle = new Bundle();
                JSONObject tempJO = ((JSONObject) result).optJSONObject("entity");
                bundle.putString("shangpin", "注册资金");
                bundle.putString("bookListId", tempJO.optString("orderid"));
                bundle.putFloat("pay_money", mPrice/100.0f);
                Intent intent = new Intent(ZhuCeZiJinActivity.this, PayOptActivity.class);
                intent.putExtra("booklistdata", bundle);
                ZhuCeZiJinActivity.this.startActivity(intent);
                ZhuCeZiJinActivity.this.finish();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });
    }

    /**
     * 根据用户id获取公司列表
     */
    private void getComListZhuCe(int uid) {
        createProgressDialog("价格获取中...");

        LogHelper.i(TAG,"注册公司变更资金---------------");
        VolleyHelpApi.getInstance().getComListYeWu(uid, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONObject jiageJO;
                JSONArray companyJA;
                try {
                    jiageJO = ((JSONObject) result).getJSONObject("price");
                    mPrice = jiageJO.optInt("ChgRgCapital");
                    LogHelper.i(TAG,"价钱--------"+mPrice);
                   // companyJA = ((JSONObject) result).getJSONArray("companyName");
                    companyJA = ((JSONObject) result).optJSONArray("companyName");
                    int compJALength = companyJA.length();
                    LogHelper.i(TAG,"公司个数："+compJALength);
                    mCompIds = new int[compJALength];
                    mCompNames = new String[compJALength];
                    for (int i = 0; i < compJALength; i++) {
                        JSONObject temp = companyJA.optJSONObject(i);
                        mCompIds[i] = temp.optInt("id");//用户ID
                        mCompNames[i] = temp.optString("name");//公司名字
                        LogHelper.i(TAG,"用户ID:"+mCompIds[i]+"------公司名字"+mCompNames[i]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissProgressDialog();
                mSelectedCompId = mCompIds[0];
                refleshCompanyView();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
                finish();
            }
        });
    }
    private void refleshCompanyView() {

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<CharSequence>(ZhuCeZiJinActivity.this, android.R.layout.simple_spinner_item, mCompNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);
        //mTVTotalMoney.setText(mPrice);
        mTVTotalMoney.setTextColor(Color.RED);
        mTVTotalMoney.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice / 100.0f));
    }

    /**
     * 创建对话框
     *
     * @param title
     */
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
     * 隐藏对话框
     */
    private void dismissProgressDialog() {
        if (mProgDoal != null) {
            mProgDoal.dismiss();
            mProgDoal = null;
        }
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        companyName = mChangeCompany.getText().toString();
        if (TextUtils.isEmpty(companyName)) {
            App.getInstance().showToast("请选择或输入公司名称");
            return;
        } else {
            switch (checkedId) {
                case R.id.change_radio_add://增

                    currentRadio = 1;
                    LogHelper.i(TAG, "增资金");
                    break;
                case R.id.change_radio_jian://减
                    currentRadio = 0;
                    LogHelper.i(TAG, "减资金");

                    break;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
