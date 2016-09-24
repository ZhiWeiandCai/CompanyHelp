package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payitem);
        ButterKnife.inject(this);

        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("返回");
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
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
