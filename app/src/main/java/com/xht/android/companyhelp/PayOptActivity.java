package com.xht.android.companyhelp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

public class PayOptActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_opt);
        Bundle bundle = getIntent().getBundleExtra("booklistdata");

        Resources resources = getResources();
        ((TextView) findViewById(R.id.ddxq)).setText(
                String.format(resources.getString(R.string.dingdanxianqing), bundle.getString("bookListId")));
        ((TextView) findViewById(R.id.ddqe)).setText(
                String.format(resources.getString(R.string.dingdanjine), bundle.getFloat("pay_money")));
    }
}
