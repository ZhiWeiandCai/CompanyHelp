package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xht.android.companyhelp.model.PersonOfSheBao;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/16.
 * 社保服务
 */
public class SheBaoSActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "SheBaoSActivity";

    private int mUId;
    private ArrayList<PersonOfSheBao> mArrayList = new ArrayList<>();
    private PersonOfSheBaoAdapter mPersonOfSheBaoAdapter;

    private ProgressDialog mProgDoal;
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
        mArrayList.add(new PersonOfSheBao());
        initView();
        mAddItemIBtn.setOnClickListener(this);
        mPersonOfSheBaoAdapter = new PersonOfSheBaoAdapter(mArrayList);
        mListView.setAdapter(mPersonOfSheBaoAdapter);
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

    /**
     * 根据用户id获取公司列表和社保价格
     */
    private void getComListAndJiaGe(int uid) {
        createProgressDialog("价格获取中...");
        VolleyHelpApi.getInstance().getComListAndJiaGeofYeWu(uid, new APIListener() {
            @Override
            public void onResult(Object result) {

            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
                finish();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_item:
                mArrayList.add(new PersonOfSheBao());
                mPersonOfSheBaoAdapter.notifyDataSetChanged();
                break;
        }
    }

    private class PersonOfSheBaoAdapter extends ArrayAdapter<PersonOfSheBao> {

        public PersonOfSheBaoAdapter(ArrayList<PersonOfSheBao> items) {
            super(SheBaoSActivity.this, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.personshebao_item, parent, false);
            }
            PersonOfSheBao item = getItem(position);

            return convertView;
        }

    }
}
