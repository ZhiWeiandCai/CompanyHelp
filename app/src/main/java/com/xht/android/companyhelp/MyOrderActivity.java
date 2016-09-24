package com.xht.android.companyhelp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016-9-23.
 *
 * author: an
 */
public class MyOrderActivity extends Activity implements View.OnClickListener {

    private String uid;

    private LinearLayout mLinearNoPay;
    private LinearLayout mLinearYesPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myorder);

        uid = getIntent().getStringExtra("uid");




    }

    @Override
    public void onClick(View v) {
        /*switch(v.getId()){
            case R.id.linear_my_nopay:
                break;
            case R.id.linear_my_yespay:
                break;

        }*/

    }
}
