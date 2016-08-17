package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/16.
 * 社保服务
 */
public class SheBaoSActivity extends Activity {

    private static final String TAG = "SheBaoSActivity";

    private int mUId;

    private Spinner mCompNameSpinner;
    private ImageButton mAddItemIBtn;
    private ListView mListView;
    private TextView mMHeJiTV;
    private Button mBookYuYue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shebaos);
        Bundle bundle = getIntent().getBundleExtra("uData");
        mUId = bundle.getInt("uid");
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("下单预约-社保服务");
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
        initView();
    }

    private void initView() {
        mCompNameSpinner = (Spinner) findViewById(R.id.spinner1);
        mAddItemIBtn = (ImageButton) findViewById(R.id.add_item);
        mListView = (ListView) findViewById(R.id.list_view);
        mMHeJiTV = (TextView) findViewById(R.id.shu_heji);
        mBookYuYue = (Button) findViewById(R.id.bookYuQue);
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
