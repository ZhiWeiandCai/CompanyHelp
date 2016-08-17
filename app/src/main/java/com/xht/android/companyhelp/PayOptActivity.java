package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xht.android.companyhelp.net.BaseApi;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;
import com.xht.android.companyhelp.util.Utils;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.LinkedHashMap;

public class PayOptActivity extends Activity {

    private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_opt);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("支付订单");
        final ActionBar aBar = getActionBar();
        int newGravity = Gravity.CENTER_HORIZONTAL;
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = lp.gravity & ~Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK | newGravity;
        aBar.setCustomView(mCustomView, lp);
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
        Bundle bundle = getIntent().getBundleExtra("booklistdata");
        final String shangPin = bundle.getString("shangpin");
        LogHelper.i("商品名称", shangPin);
        final String dingdanHao = bundle.getString("bookListId");
        final float jinE = bundle.getFloat("pay_money");
        Resources resources = getResources();
        ((TextView) findViewById(R.id.shangpin)).setText(shangPin);
        ((TextView) findViewById(R.id.ddxq)).setText(
                String.format(resources.getString(R.string.dingdanxianqing), dingdanHao));
        ((TextView) findViewById(R.id.ddqe)).setText(
                String.format(resources.getString(R.string.dingdanjine), bundle.getFloat("pay_money")));

        mIWXAPI = WXAPIFactory.createWXAPI(this, null);
        mIWXAPI.registerApp("");
        findViewById(R.id.zhifu_liji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //立即支付
                String url = VolleyHelpApi.MakeURL(BaseApi.WEIXI_LJZF_URL, new LinkedHashMap<String, Object>() {
                    {
                        put("dingdanHao", dingdanHao);
                        put("jinE", jinE);
                        put("shangpin", URLEncoder.encode(shangPin));
                    }
                });
                Button payBtn = (Button) findViewById(R.id.zhifu_liji);
                payBtn.setEnabled(false);
                Toast.makeText(PayOptActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
                try{
                    byte[] buf = Utils.httpGet(url);
                    if (buf != null && buf.length > 0) {
                        String content = new String(buf);
                        Log.e("get server pay params:",content);
                        JSONObject json = new JSONObject(content);
                        if(null != json && !json.has("retcode")){
                            PayReq req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId			= json.getString("appid");
                            req.partnerId		= json.getString("partnerid");
                            req.prepayId		= json.getString("prepayid");
                            req.nonceStr		= json.getString("noncestr");
                            req.timeStamp		= json.getString("timestamp");
                            req.packageValue	= json.getString("package");
                            req.sign			= json.getString("sign");
                            req.extData			= "app data"; // optional
                            Toast.makeText(PayOptActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            mIWXAPI.sendReq(req);
                        }else{
                            Log.d("PAY_GET", "返回错误"+json.getString("retmsg"));
                            Toast.makeText(PayOptActivity.this, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Log.d("PAY_GET", "服务器请求错误");
                        Toast.makeText(PayOptActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Log.e("PAY_GET", "异常："+e.getMessage());
                    Toast.makeText(PayOptActivity.this, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                payBtn.setEnabled(true);
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

    private class ZhiFuTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }
}
