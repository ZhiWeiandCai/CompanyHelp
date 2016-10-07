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
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xht.android.companyhelp.util.LogHelper;

/**
 * Created by Administrator on 2016-9-23.
 *
 * author: an
 */
public class MyOrderActivity extends Activity implements View.OnClickListener {

    private int uid;

    private RadioGroup mButGroup;
    private RadioButton mButNoPay;
    private RadioButton mButYesPay;

    private FrameLayout mFragmePay;
    private Fragment mFragmeNoPay;
    private Fragment mFragmeYesPay;
    private ProgressDialog mProgDoal;
    private FragmentManager fragmentManager;

    private GestureDetector mGesture; //手势识别
    private int currentIndex;
    private static final String TAG = "MyOrderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_myorder);

      //  mGesture=new GestureDetector(this, new MyOnGestureListener());
        TextView textView=new TextView(this);
        textView.setText("我的订单");
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(18);
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(textView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        actionBar.setDisplayOptions(change);

        Bundle bundle = getIntent().getBundleExtra("mBundle");
        uid = bundle.getInt("mUid",0);
        LogHelper.i(TAG,"-----"+ this.uid);//用户id

        mButGroup= (RadioGroup) findViewById(R.id.switch_tabpay);
        mButNoPay = (RadioButton) findViewById(R.id.but_nopay);
        mButYesPay= (RadioButton) findViewById(R.id.but_yespay);
        mFragmePay= (FrameLayout) findViewById(R.id.fram_content);

        mButNoPay.setOnClickListener(this);
        mButYesPay.setOnClickListener(this);
        fragmentManager = getFragmentManager();

        selectCurFragment(0);

    }

    /**
     * 选择未支付与支付的页面
     * @param index
     */
    public void selectCurFragment(int index) {

        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        switch (index) {
            case 0:
                if (mFragmeNoPay == null) {
                    // 如果mFragmeNoPay为空，则创建一个并添加到界面上
                    mFragmeNoPay=new NoPayFragment();

                    Bundle bundle=new Bundle();
                    bundle.putInt("uid",uid);
                    mFragmeNoPay.setArguments(bundle);
                    transaction.add(R.id.fram_content, mFragmeNoPay);

                } else {
                    // 如果mFragmeNoPay不为空，则直接将它显示出来
                    transaction.show(mFragmeNoPay);
                    transaction.hide(mFragmeYesPay);
                }
                mButNoPay.setTextColor(Color.BLUE);
                mButYesPay.setTextColor(Color.GRAY);
                break;
            case 1:
                if (mFragmeYesPay == null) {
                    // 如果mFragmeYesPay为空，则创建一个并添加到界面上
                    mFragmeYesPay = new YesPayFragment();

                    Bundle bundle=new Bundle();
                    bundle.putInt("uid",uid);
                    mFragmeYesPay.setArguments(bundle);
                    transaction.add(R.id.fram_content, mFragmeYesPay);
                } else {
                    // 如果mFragmeYesPay不为空，则直接将它显示出来
                    transaction.show(mFragmeYesPay);
                    transaction.hide(mFragmeNoPay);
                }
                mButNoPay.setTextColor(Color.GRAY);
                mButYesPay.setTextColor(Color.BLUE);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {

        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况

        switch(v.getId()){
            case R.id.but_nopay://未支付
                currentIndex=0;

                break;
            case R.id.but_yespay://已支付
                currentIndex=1;
                break;

        }
        selectCurFragment(currentIndex);


    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mFragmeNoPay != null) {
            transaction.hide(mFragmeNoPay);
        }
        if (mFragmeYesPay != null) {
            transaction.hide(mFragmeNoPay);
        }

    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        mFragmePay.setBackgroundColor(0xffffffff);

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


}
