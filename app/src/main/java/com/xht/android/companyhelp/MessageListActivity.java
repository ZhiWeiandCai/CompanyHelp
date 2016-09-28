package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xht.android.companyhelp.model.UserInfo;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Administrator on 2016-9-8.
 * author: an
 */
public class MessageListActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.mess_list_title)//标题
    TextView mTextTitle;
    @InjectView(R.id.mess_list_content)//内容
    TextView mTextContent;
    @InjectView(R.id.mess_list_income)//收入
    TextView mTextIncome;
    @InjectView(R.id.mess_list_added_tax)//增值税
    TextView mTextTax;
    @InjectView(R.id.mess_list_development_tax)//文化建设税
    TextView mTextDeve;
    @InjectView(R.id.mess_list_company_tax)//企业所得税
    TextView mTextCompany;
    @InjectView(R.id.mess_list_declare_tax)//通用申报
    TextView mTextDeclare;
    @InjectView(R.id.mess_list_personal_tax)//个税
    TextView mTextPersonal;

    @InjectView(R.id.mess_list_notpass)//不通过
    Button mButNotPass;
    @InjectView(R.id.mess_list_pass)//通过
    Button mButPass;

    private static final String TAG = "MessageListActivity";
    private String value; //消息的id
    private String title;//标题
    private String personalTax;//个人所得税
    private String generalDeclareTax;//通用申报
    private String bizIncomTax;//企业所得税
    private String income;//收入
    private String culturalConTax;//文化建设税
    private String detailText;//内容
    private String addedValueTax;//增值税
    private SharedPreferences mSharedPreference;

    private ProgressDialog mProgDoal;
    private UserInfo mUseInfo;
    private int mUid;//用户id

    private int flag;
    private int trueFlag=0;
    private int mMessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_mess_activity);
      /*  bundle.putString("messId",MessageUid);
        intent.putExtra("MessUid",bundle);
*/
        mSharedPreference=getSharedPreferences("shared",MODE_PRIVATE);

        Bundle messUid = getIntent().getBundleExtra("MessUid");
        if (messUid!=null){
            String messId=messUid.getString("messId");
            mUid = messUid.getInt("mUid");
            mMessId = Integer.valueOf(messId).intValue();
            LogHelper.i(TAG, "--333--" + messId + "--"+ mMessId +"---"+ mUid);
            getComList(mMessId);

        }

        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("确认税情-返回");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;

        aBar.setDisplayOptions(change);
        ButterKnife.inject(this);
        mButNotPass.setOnClickListener(this);
        mButPass.setOnClickListener(this);

    }


    //获取公司名称
    /**
     * 根据用户id获取公司列表
     */
    private void getComList(int uid) {
        createProgressDialog("正在加载税金金额...");
        VolleyHelpApi.getInstance().getYouMengYeWu(uid, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i(TAG, "-----消息的所有信息----" + result.toString());
                dismissProgressDialog();
                //{"title":"null年1月份报税金额确认","personalTax":3,"income":3,"generalDeclareTax":3,"bizIncomTax":3,"CulturalConTax":3, "detailText":"尊敬的【广药大】，小后台为贵公司null年1月份的报税情况如下，如确认无误，请点击确认，以便我司尽快给您报税。", "addedValueTax":3},"code":"1"}

              JSONObject jsonJO=(JSONObject) result;
                title = jsonJO.optString("title");//标题
                detailText = jsonJO.optString("detailText");//内容
                personalTax = jsonJO.optString("personalTax");//个人所得税
                generalDeclareTax = jsonJO.optString("generalDeclareTax");//通用申报
                bizIncomTax = jsonJO.optString("bizIncomTax");//企业所得税
                income = jsonJO.optString("income");//收入
                culturalConTax = jsonJO.optString("CulturalConTax");//文化建设税
                addedValueTax = jsonJO.optString("addedValueTax");//增值税
                surePost();
                LogHelper.i(TAG, title +"----"+ personalTax +"--"+ income);

            }
            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
                finish();
            }
        });
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
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCoder,KeyEvent event) {
        if (keyCoder == KeyEvent.KEYCODE_BACK) {
          //  mWebViewe.goBack();   //goBack()表示返回webView的上一页面
            finish();
            return true;
        }
        return false;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Bundle bun = getIntent().getExtras();
        if (bun != null)
        {
            Set<String> keySet = bun.keySet();
            for (String key : keySet) {
                value = bun.getString(key);
                if (value!=null) {
                     mMessId = Integer.valueOf(value).intValue();
                    LogHelper.i(TAG, "--333--" + value + "--" + mMessId);
                    getComList(mMessId);
                }
            }
        }
        mUseInfo=MainActivity.getInstance();
        mUid = mUseInfo.getUid();
        LogHelper.i(TAG,"---"+ mUid +"-4444444-");

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mess_list_pass://通过
                flag =1;
                break;
            case R.id.mess_list_notpass://不通过
                flag=0;
                break;
        }
         trueFlag = mSharedPreference.getInt("trueFlag", -1);

        if (trueFlag ==1){
            App.getInstance().showToast("你已经确认过报税请求，无需再确认。");
            MessageListActivity.this.finish();
            return;

        }else{
            //确认提交
            postDataSui();
        }
    }

    //点击通过
    private void postDataSui() {
        JSONObject jsonJO=new JSONObject();
        try {
            jsonJO.put("confirmFlag",flag);
            jsonJO.put("userid", mUid);
            jsonJO.put("taxid", mMessId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        createProgressDialog("正在提交...");
        //报税请求---提交
        VolleyHelpApi.getInstance().postYouMengMessage(mUid, jsonJO, new APIListener() {
            @Override
            public void onResult(Object result) {
                dismissProgressDialog();
                trueFlag=1;

               SharedPreferences.Editor edit =mSharedPreference.edit();
                edit.putInt("trueFlag",trueFlag);
                edit.commit();

                LogHelper.i(TAG,"--通过");
                App.getInstance().showToast("请求报税成功通过！");
                MessageListActivity.this.finish();
            }
            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });
    }
    //展示数据
    private void surePost() {
        mTextTitle.setText(title);
        mTextContent.setText(detailText);
        mTextIncome.setText(income);
        mTextTax.setText(addedValueTax);
        mTextDeve.setText(culturalConTax);
        mTextCompany.setText(bizIncomTax);
        mTextDeclare.setText(generalDeclareTax);
        mTextPersonal.setText(personalTax);
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