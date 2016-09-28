package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016-9-27.
 */
public class ZhangHuMiMa extends Activity  {

    private int mUId;
    private String getNumber;
    private ProgressDialog mProgDoal;
    private static final String TAG = "ZhangHuMiMa";
    private EditText edOldMiMa;
    private EditText edNewMiMa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhanghu_mima);

        Bundle bundle = getIntent().getBundleExtra("mBundle");
        mUId = bundle.getInt("mUid",0);

        LogHelper.i(TAG,"---"+mUId);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("修改账户");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        getZhangHuXinXi(mUId);
        edOldMiMa = (EditText) findViewById(R.id.ed_old_mima);
        edNewMiMa = (EditText) findViewById(R.id.ed_new_mima);

        Button butSumit= (Button) findViewById(R.id.zh_submit1);
      butSumit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              postButtonSubmit();
          }
      });
    }

    private void postButtonSubmit() {
        JSONObject MessJson=new JSONObject();
        String oldPass = edOldMiMa.getText().toString();
        String newPass = edNewMiMa.getText().toString();
        if (TextUtils.isEmpty(oldPass)){
            App.getInstance().showToast("旧密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(newPass)){
            App.getInstance().showToast("请输入新的密码");
            return;
        }
        LogHelper.i(TAG,"-------------------");
        try {
            MessJson.put("userId",mUId);
            MessJson.put("oldPass",oldPass);
            MessJson.put("newPass",newPass);

            LogHelper.i(TAG,"-------------------"+MessJson.toString());
        }catch(JSONException e) {
            e.printStackTrace();
        }

        //提交账户信息
        createProgressDialog("密码修改中中...");
        VolleyHelpApi.getInstance().postChangMiMa(mUId, MessJson, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i(TAG,"修改密码成功");
                App.getInstance().showToast("密码修改成功");

                ZhangHuMiMa.this.finish();
            }
            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });
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
     * 隐藏对话框
     */
    private void dismissProgressDialog() {
        if (mProgDoal != null) {
            mProgDoal.dismiss();
            mProgDoal = null;
        }
    }
    /**
     * 获取账户信息
     * @param uid
     */

    private void getZhangHuXinXi(int uid) {
        VolleyHelpApi.getInstance().getZhangHuGuanLiYeWu11(uid, new APIListener() {
            @Override
            public void onResult(Object result) {
                // {"contactName":"蔡成安","loginStatus":"1","ordContactId":12,"password":"123456","registTime":"1471674887613","status":"1","telephone":"13531829360"}}
                LogHelper.i(TAG,"---mima---"+result.toString()+"--");
                JSONObject mJSONJO;
                try {
                    mJSONJO = ((JSONObject) result).getJSONObject("user");
                    getNumber=mJSONJO.optString("password");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
                finish();
            }
        });
    }
}
