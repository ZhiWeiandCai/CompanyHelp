package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xht.android.companyhelp.model.PayDetail;
import com.xht.android.companyhelp.model.PayResult;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.BaseApi;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;
import com.xht.android.companyhelp.util.Utils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;



public class PayOptActivity extends Activity implements View.OnClickListener {

	private static final String TAG = "PayOptActivity";
    public static final String BRO_PAY_S = "com.xht.android.companyhelp.bro_pay_s";
	/** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016091201891549";

    /** 商户私钥，pkcs8格式 */
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAPLWpXGVAaVVkqk2jaOYXW3HhYkTE2cev8hrJej9NXyvRXvM9jKzsZV/g9ytMcWatQnr17peRR9ahgrcZpZP77fl3/MK306UC7obEH87QMJi/ZhDE5HPMlhWmIRGGWhQ/9Pbgrpe0W4L8IXKyF5MCpmHNISXcQ3z3RosbO1JL9CJAgMBAAECgYEAg163dfDESSs4Ai6vBTeiI4dyqCsXrgumeMfuZFLHIsK29jh1YAxyH4wyS6muaVY+ZjWeKQZioomYRjvhi6OG7Im34IB1fsa01nuEdj0qrlqM2tADLQrPY5QLsFOo+je2EWH4tUnXc7o45ZfFKCY7m9R9Jv2a40hb1VkIhw9OGAECQQD8HtfXcbHaN4z51jNNdn3fOsAEovwjwHv5zfSrlJ4XNmXp9gN63Jd5ALB1ebEpEGDusMZ95PqOQtwXFMI+6I4JAkEA9pM9QDzSHR18ZS5xLO9e49R7aMgXS2Djt5azIj4Z1dnxPjtmrgcF16/nPE3ajwsECxlMxhtEzksirpw6o03OgQJBAPbTt1psK8PIuNLreuRuigKewNiJPDJAQt+oW84TPMba8eGggX2qMv06yIbqMaBTMaLdHCaqowXdvR03rIfsIwkCQGHwQ+DzB4YXAyTxWHS/s1INQONyroX7Oxfd3NMaFYryoTbVw0gWdASi0tcKGWi9uGJsLyl1BVRnVaRDW6snqYECQB75QIiKR/zXstIsrpvN5gD6w0T7kek1mNGP54Du4CCO3TFbHrbmHLtia+rMKAPBl4pfIb7n8QgV6QR9EXsy1TI=";
    //商户合作id
    public static final String PARTNER = "2088422875013033";
    // 商户收款的支付宝账号
    public static  String SELLER ;

    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQsKBgQDy1qVxlQGlVZKpNo2jmF1tx4WJExNnHr/IayXo/TV8r0V7zPYys7GVf4PcrTHFmrUJ69e6XkUfWoYK3GaWT++35d/zCt9OlAu6GxB/O0DCYv2YQxORzzJYVpiERhloUP/T24K6XtFuC/CFysheTAqZhzSEl3EN890aLGztSS/QiQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;
    public static final String PAY_STATUS = "s_key";
    private IWXAPI mIWXAPI;
	private LinearLayout mLinearAliPay;//支付宝
    private LinearLayout mLinearWePay;//微信
    private Button payBtn;

	private int mPayFlag=1;
    private String shangPin;
    private String dingdanHao;
    private float jinE;
    private int YeWuStyle;


    //支付成功的消息的集合
    public static ArrayList<PayDetail> mPayList=new ArrayList();
    private String payInfo;
    private String time;
    private ImageView mImageWeXin;
    private ImageView mImageZhiFu;
    private int position;
    private String weizhifu;

    public static ArrayList<PayDetail> getPayList() {
        return mPayList;
    }

   private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    LogHelper.i(TAG, "------------" + resultInfo);

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PayOptActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        if (YeWuStyle == 10) {
                            App.getInstance().showToast("感谢盆友支持小后台,请在“我的”-“完善资料”中" +
                                    "完善您公司注册的具体信息，谢谢合作！");
                        }
                        Intent intent=new Intent(PayOptActivity.this,PayItemActivity.class);
                        intent.putExtra("time",time);
                        intent.putExtra("goods",shangPin);
                        intent.putExtra("number",dingdanHao);
                        intent.putExtra("money",jinE+"");
                        intent.putExtra("busy",SELLER);
                        intent.putExtra("flag",mPayFlag);
                        intent.putExtra("position",position);
                        intent.putExtra("weizhifu",weizhifu);

                        payListData();
                        LogHelper.i(TAG,"-------------：：："+time+shangPin+dingdanHao+mPayFlag);

                        startActivity(intent);
                        PayOptActivity.this.finish();

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayOptActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayOptActivity.this, "支付失败，到我的订单中查看", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }

                default:
                    break;
            }
        };
    };

    private void payListData() {
        PayDetail itempay=new PayDetail();
        time = Utils.getTimeUtils(System.currentTimeMillis());
        itempay.setmTime(time);
        itempay.setmTitle(shangPin);
        itempay.setmOrderNumber(dingdanHao);
        itempay.setmTotalMoney(jinE+"");
        itempay.setMethod(mPayFlag);
        if (mPayFlag==0){
            itempay.setBusyer(SELLER);
        }
        if (mPayFlag==1){
            itempay.setBusyer("小后台财税服务有限公司");//TODO 微信账号
        }
        mPayList.add(itempay);
    }
    private BroadcastReceiver mPayStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int temp = intent.getIntExtra(PAY_STATUS, 0);
            if (temp == 0) {
                if (YeWuStyle == 10) {
                    App.getInstance().showToast("感谢盆友支持小后台,请在“我的”-“完善资料”中" +
                            "完善您公司注册的具体信息，谢谢合作！");
                }
                Intent i=new Intent(PayOptActivity.this,PayItemActivity.class);
                i.putExtra("time",time);
                i.putExtra("goods",shangPin);
                i.putExtra("number",dingdanHao);
                i.putExtra("money",jinE+"");
                i.putExtra("busy","小后台财税服务有限公司");
                i.putExtra("flag",mPayFlag);
                i.putExtra("position",position);
                i.putExtra("weizhifu",weizhifu);

                payListData();
                startActivity(intent);

                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_opt);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setTextSize(18);
        mCustomView.setText("支付订单");
        final ActionBar aBar = getActionBar();
        int newGravity = Gravity.CENTER_HORIZONTAL;
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = lp.gravity & ~Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK | newGravity;
        aBar.setCustomView(mCustomView, lp);
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
        Bundle bundle = getIntent().getBundleExtra("booklistdata");
        shangPin = bundle.getString("shangpin");
        weizhifu = bundle.getString("weizhifu");

        LogHelper.i("------商品名称", shangPin+"---"+ weizhifu);
        //判断业务类型
        switchMethod(shangPin);


        LogHelper.i("--商品名称", shangPin);
        dingdanHao = bundle.getString("bookListId");
        position = bundle.getInt("position");
        jinE = bundle.getFloat("pay_money");

        LogHelper.i("------商品名称", "---shangPin"+shangPin+"--hasAccount-"+ weizhifu +"--dingdanHao"+dingdanHao+"--jinE"+jinE);

        Resources resources = getResources();
        ((TextView) findViewById(R.id.shangpin)).setText(shangPin);
        ((TextView) findViewById(R.id.ddxq)).setText(
                String.format(resources.getString(R.string.dingdanxianqing), dingdanHao));
        ((TextView) findViewById(R.id.ddqe)).setText(
                String.format(resources.getString(R.string.dingdanjine), bundle.getFloat("pay_money")));

        mLinearAliPay= (LinearLayout) findViewById(R.id.alipay_pay);
        mLinearWePay= (LinearLayout) findViewById(R.id.wepay_pay);
        payBtn = (Button) findViewById(R.id.zhifu_liji);



        mImageWeXin = (ImageView) findViewById(R.id.we_xin);
        mImageZhiFu = (ImageView) findViewById(R.id.zhi_fu);

        mLinearAliPay.setOnClickListener(this);
        mLinearWePay.setOnClickListener(this);

        mIWXAPI = WXAPIFactory.createWXAPI(this, null);
        mIWXAPI.registerApp("wx9d3b007949c52dd8");
        payBtn = (Button) findViewById(R.id.zhifu_liji);
		payBtn.setOnClickListener(this);
        
        IntentFilter intentFilter = new IntentFilter(BRO_PAY_S);
        registerReceiver(mPayStatus, intentFilter);
    }
	
	/**
     * 注册公司 1；
     记账报税 2；
     社保服务 3；
     发票服务 4；
     注册商标 5；
     雇主保险 6；
     变更服务 7；
     注册资金 8；
     注销服务 9；
     劳务派遣 10
     * @param shangPin
     */
    private void switchMethod(String shangPin) {
        switch (shangPin){
            case "注册公司":
                YeWuStyle=10;
                break;
            case "记账报税":
                YeWuStyle=11;
                break;
            case "社保服务":
                YeWuStyle=12;
                break;
            case "发票服务":
                YeWuStyle=13;
                break;
            case "注册商标":
                YeWuStyle=14;
                break;
            case "雇主保险":
                YeWuStyle=15;
                break;
            case "变更服务":
                YeWuStyle=16;
                break;
            case "注册资金":
                YeWuStyle=17;
                break;
            case "注销服务":
                YeWuStyle=18;
                break;
            case "劳务派遣":
                YeWuStyle=19;
                break;
        }
        LogHelper.i(TAG,"---------"+YeWuStyle);
    }
	
	@Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.alipay_pay:
                mPayFlag=0;


                mImageWeXin.setVisibility(View.GONE);
                mImageZhiFu.setVisibility(View.VISIBLE);

                break;
            case R.id.wepay_pay:
                mPayFlag=1;

                mImageWeXin.setVisibility(View.VISIBLE);
                mImageZhiFu.setVisibility(View.GONE);

                break;
            case R.id.zhifu_liji:

                //支付提交
                PayOfMethod();
                break;
        }
    }
	
	private void PayOfMethod() {
        if (mPayFlag==0){
            //选择支付宝支付

            App.getInstance().showToast("支付宝方式");
            payOfAlipayMethod();
        }else if(mPayFlag==1){
            //选择微信支付

            payOfWeMethod();
            App.getInstance().showToast("微信方式");
        }else{
            App.getInstance().showToast("请选择支付方式");
            return;
        }
    }
	
	/**
     * 支付宝
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void payOfAlipayMethod() {
        /**
         * call alipay sdk pay. 调用SDK支付
         *
         */

        getAlipay();


    }

    private void getAlipay() {
        VolleyHelpApi.getInstance().getDingDanMessage12(shangPin,dingdanHao,jinE+"",YeWuStyle, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONObject resultjson=(JSONObject)result;

                String  sign=resultjson.optString("sign");
                String  orderInfo=resultjson.optString("entity");

                  SELLER=resultjson.optString("user_id");
                LogHelper.i(TAG,"---2222"+sign);
                LogHelper.i(TAG,"---2222"+SELLER);
                LogHelper.i(TAG,"--8888"+orderInfo);
                try {
                    // 仅需对sign 做URL编码
                    sign = URLEncoder.encode(sign, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                LogHelper.i(TAG,"-------"+sign);

                /**
                 * 完整的符合支付宝参数规范的订单信息
                 */
                payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
                Log.i(TAG,"----"+ payInfo);

                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(PayOptActivity.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(payInfo, true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }

            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
                finish();
            }
        });
    }


    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }


    //微信支付
    private void payOfWeMethod() {
        //立即支付
                String url = VolleyHelpApi.MakeURL(BaseApi.WEIXI_LJZF_URL, new LinkedHashMap<String, Object>() {
                    {
                        put("dingdanHao", dingdanHao);
                        put("jinE", jinE);
                        put("shangpin", URLEncoder.encode(shangPin));
                        put("YeWuStyle", YeWuStyle);
                    }
                });
                payBtn.setEnabled(false);
                Toast.makeText(PayOptActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
                new ZhiFuTask().execute(url);
        
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mPayStatus);
        super.onDestroy();
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

    private class ZhiFuTask extends AsyncTask<String, Void, byte[]> {

        @Override
        protected byte[] doInBackground(String... params) {
            byte[] buf = Utils.httpGet(params[0]);
            return buf;
        }

        @Override
        protected void onPostExecute(byte[] result) {
            try{
                if (result != null && result.length > 0) {
                    String content = new String(result);
                    Log.e("get server pay params:",content);
                    JSONObject json = new JSONObject(content);
                    if(null != json && !json.has("retcode")){
                        PayReq req = new PayReq();
                        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                        req.appId = json.getString("appid");
                        req.partnerId = json.getString("partnerid");
                        req.prepayId = json.getString("prepayid");
                        req.nonceStr = json.getString("noncestr");
                        req.timeStamp = json.getString("timestamp");
                        req.packageValue = "Sign=WXPay";
                        req.sign = json.getString("sign");
                        //req.extData = "app data"; // optional

                        //在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                        boolean tempTest = mIWXAPI.sendReq(req);
                        if (tempTest) {
                            Toast.makeText(PayOptActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PayOptActivity.this, "您是否安装了微信", Toast.LENGTH_SHORT).show();
                        }
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
    }
}
