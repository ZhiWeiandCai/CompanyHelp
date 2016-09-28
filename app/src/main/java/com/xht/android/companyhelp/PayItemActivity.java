package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xht.android.companyhelp.util.LogHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016-9-20.
 *
 * author: an
 */
public class PayItemActivity extends Activity{

    @InjectView(R.id.pay_item_time)
     TextView mPayItemTime;
    @InjectView(R.id.pay_item_money)
     TextView mPayItemMoney;
    @InjectView(R.id.pay_item_goods)
     TextView mPayItemGoods;
    @InjectView(R.id.pay_item_number)
     TextView mPayItemNumber;
    @InjectView(R.id.pay_item_flag)
     TextView mPayItemFlag;
    @InjectView(R.id.pay_item_busyer)
    TextView mPayItemBusy;

    private static final String TAG = "PayItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payitem);
        ButterKnife.inject(this);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("返回");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String money = intent.getStringExtra("money");
        String shangPin = intent.getStringExtra("goods");
        String dingdanHao = intent.getStringExtra("number");
        String weizhifu = intent.getStringExtra("weizhifu");
        int mPayFlag = intent.getIntExtra("flag", -1);
        String busy =intent.getStringExtra("busy");

        if (mPayFlag==0){
            mPayItemFlag.setText("支付宝支付");
        }else if (mPayFlag==1){
            mPayItemFlag.setText("微信支付");

        }

        mPayItemTime.setText(time);
        mPayItemMoney.setText(money);
        mPayItemGoods.setText(shangPin);
        mPayItemNumber.setText(dingdanHao);
        mPayItemBusy.setText(busy);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (!TextUtils.isEmpty("weizhifu")){
                   startActivity(new Intent(PayItemActivity.this,MainActivity.class));
                    finish();

                }else{
                    finish();
                }

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
        //这里重写返回键
            if (!TextUtils.isEmpty("weizhifu")){
                startActivity(new Intent(PayItemActivity.this,MainActivity.class));
                LogHelper.i(TAG,"-------fanhui");
                finish();

            }else{
                LogHelper.i(TAG,"-------fan");
                finish();
            }
            return true;
        }
        return false;

    }

}
