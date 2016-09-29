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
import android.widget.Spinner;
import android.widget.TextView;

import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/21.
 * author: an
 */
public class CancelComActivity extends Activity {


    private EditText mETCompanyName;//输入的公司名称
    private Spinner mSpCompanyName; //从数据库中拿下来的公司名称
    private TextView mTVTotalMoney;//公司对应的注册金额
    private Button mButtonSend;//按钮

    private int mUId;//用户id
    private String[]mCompNames;//公司名字数组
    private int[] mCompIds;
    private int mSelectedCompId;//选中哪一个id
    private  int mPrice;

    private ProgressDialog mProgDoal;//对话框
    private static final String TAG = "CancelComActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getBundleExtra("uData");
        mUId = bundle.getInt("uid",0);
        setContentView(R.layout.activity_cancelcompany);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("下单预约-注销公司");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        mETCompanyName= (EditText) findViewById(R.id.company_name_cancel);
        mSpCompanyName= (Spinner) findViewById(R.id.company_name_spinner);
        mTVTotalMoney= (TextView) findViewById(R.id.company_money_total);
        mButtonSend= (Button) findViewById(R.id.company_money_cancel_button);

        mETCompanyName.setEnabled(false);
        getComList(mUId);
        initData();

     //   showTotalMoney();
    }

    private void initData() {
        //获取用户公司名称
        mSpCompanyName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedCompId = mCompIds[position];
                LogHelper.i(TAG, mCompNames[position]);
                mSelectedCompId=mCompIds[position];
                mETCompanyName.setText(mCompNames[position].toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName=mETCompanyName.getText().toString();
                if (TextUtils.isEmpty(companyName)){
                    App.getInstance().showToast("请输入或选择公司名称");
                    return;
                }
                LogHelper.i(TAG,companyName+"----公司名称");
                //TODO
                //注销公司，把有关这个公司的相关信息从数据库中删除
                deleteCompanyJSONPost();

            }
        });
    }
    public void deleteCompanyJSONPost() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("CompanyName", mETCompanyName.getText().toString());
            //obj.put("CompanyId", mSelectedCompId);//公司Idid
            obj.put("CustomerId",mUId);//下单用户Id
            obj.put("Price",mPrice);
            LogHelper.i(TAG,"---下单用户id-----"+mUId+"--公司Id:---"+mSelectedCompId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        createProgressDialog("订单提交中...");
        LogHelper.i(TAG, "----注销公司提交的所有信息" + obj.toString());

        VolleyHelpApi.getInstance().postDingDanDelete(mUId, obj, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i("订单提交成功", "2016-08-23");
                LogHelper.i(TAG,"注销公司所有信息"+result.toString()+"---");
                dismissProgressDialog();
                Bundle bundle = new Bundle();
                JSONObject tempJO = ((JSONObject) result).optJSONObject("entity");
                bundle.putString("shangpin","注销服务");
                bundle.putString("bookListId", tempJO.optString("orderId"));
                bundle.putFloat("pay_money", mPrice/100.0f);
                Intent intent = new Intent(CancelComActivity.this, PayOptActivity.class);
                intent.putExtra("booklistdata", bundle);
                CancelComActivity.this.startActivity(intent);
                CancelComActivity.this.finish();
            }
            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }

        });
    }
    /**
     *  根据写入的公司名称查找服务器中储存的公司名字，
     * 显示注销公司提交定单的金额费用
     */
    private void showTotalMoney() {
        mTVTotalMoney.setTextColor(Color.RED);
        mTVTotalMoney.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice/100.0f));

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
     * 根据用户id获取公司列表TODO和注销公司价格
     */
    private void getComList(int uid) {
        createProgressDialog("价格获取中...");
        VolleyHelpApi.getInstance().getZhuXiaoComListYeWu(uid, new APIListener() {

            @Override
            public void onResult(Object result) {
                LogHelper.i(TAG, "--注销公司所有信息--" + result.toString());
                JSONObject jiageJO;
                JSONArray companyJA;
                try {
                   jiageJO = ((JSONObject) result).getJSONObject("price");
                    mPrice= jiageJO.optInt("CancelCompany");//注销公司价格
                    LogHelper.i(TAG,"价钱----"+mPrice);
                    //companyJA = ((JSONObject) result).getJSONArray("companyName");
                    companyJA = ((JSONObject) result).optJSONArray("companyName");
                    LogHelper.i(TAG, "---所有信息--" + companyJA.toString());
                    int compJALength = companyJA.length();
                    mCompIds = new int[compJALength];
                    //LogHelper.i(TAG, "--公司个数：" + compJALength);
                    mCompNames = new String[compJALength];
                    for (int i = 0; i < compJALength; i++) {
                        JSONObject temp = companyJA.optJSONObject(i);

                        mCompIds[i] = temp.optInt("id");
                        mCompNames[i] = temp.optString("name");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissProgressDialog();
                mSelectedCompId = mCompIds[0];

                //显示提交订单金额
                showTotalMoney();
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

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<CharSequence>(CancelComActivity.this, android.R.layout.simple_spinner_item, mCompNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpCompanyName.setAdapter(arrayAdapter);
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

}
