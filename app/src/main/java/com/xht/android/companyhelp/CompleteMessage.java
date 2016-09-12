package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xht.android.companyhelp.model.CompleteJavaBean;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-8-30.完善资料
 */
public class CompleteMessage extends Activity implements View.OnClickListener {

    private EditText mCCName;//输入公司名称
    private EditText mCMoney;//输入注册金额
    private Spinner mCSpinner;//选择公司类型
    private EditText mJYAddress;//经营地址
    private EditText mJYFanWei;//经营范围

    private ProgressDialog mProgDoal;

    private ListView mListComplete;
    private Button mButAdd;
    private Button mButCancel;

    private int mCompanyId=0;
    private int mWorkId=0;
    private int mUId;//用户id
    private ArrayList<CompleteJavaBean> mListData=new ArrayList<>();//数据存储
    private CompleteAdapter adapter;//每个item存储的信息

    private String[] mCompanyNames={"有限责任公司(自然人投资或控股)","有限责任公司(有限人独资)"};
    private String[] mWorks={"无职位","经理","监事","法人"};
    private static final String TAG = "CompleteMessage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getBundleExtra("mBundle");
        mUId = bundle.getInt("mUid",0);
        LogHelper.i(TAG,"--"+mUId);


        setContentView(R.layout.acticity_complete);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("完善信息");
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        mListComplete= (ListView) findViewById(R.id.complete_xinxi_list);
        mButAdd= (Button) findViewById(R.id.but_item_sure);
        mButCancel= (Button) findViewById(R.id.but_item_cancel);
        mButAdd.setOnClickListener(this);
        mButCancel.setOnClickListener(this);
        //添加listview的头


        View viewHeader=View.inflate(CompleteMessage.this,R.layout.complete_list_header,null);
        mCCName= (EditText) viewHeader.findViewById(R.id.complete_company_name);
        mCMoney= (EditText) viewHeader.findViewById(R.id.complete_zhuce_money);
        mCSpinner= (Spinner) viewHeader.findViewById(R.id.complete_company_leixing);
        mJYAddress= (EditText) viewHeader.findViewById(R.id.complete_jingying_address);
        mJYFanWei= (EditText) viewHeader.findViewById(R.id.complete_company_fanwei);
        mListComplete.addHeaderView(viewHeader);
        adapter=new CompleteAdapter(mListData);
        mListComplete.setAdapter(adapter);
        initData();

        mListData.add(new CompleteJavaBean());
        mListData.add(new CompleteJavaBean());

        mListComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(CompleteMessage.this,CompleteItemMessage.class);
                intent.putExtra("ItemWhich",position-1);
                CompleteMessage.this.startActivityForResult(intent,0);
            }
        });
    }


    /*
    *打开意图回调的方法，接受信息
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){
          //  LogHelper.i(TAG,"---item所有信息"+data.toString()+"------");

            if (resultCode==RESULT_OK){
                int mWhich=data.getIntExtra("ItemWhich",-1);
               String name= data.getStringExtra("mName");
               String bili= data.getStringExtra("mBiLi");
               String phone= data.getStringExtra("mSFZPhone");
               String address= data.getStringExtra("mSFZAddress");
               String job= data.getStringExtra("mZhiWei");
                LogHelper.i(TAG,mWhich+"---------"+name+"---"+bili+"----"+phone+"---"+address+"--"+job);

                mListData.get(mWhich).setmName(name);
                mListData.get(mWhich).setmBiLi(bili);
                mListData.get(mWhich).setmNumber(phone);
                mListData.get(mWhich).setmAddress(address);
                mListData.get(mWhich).setmPWork(job);
                adapter.notifyDataSetChanged();
            }
        }
    }
    //填充数据
    private void initData() {
        ArrayAdapter<CharSequence> mCSpinnerAdapter=new ArrayAdapter<CharSequence>(CompleteMessage.this,android.R.layout.simple_spinner_item,mCompanyNames);
        mCSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCSpinner.setAdapter(mCSpinnerAdapter);
        mCSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCompanyId=position;
                LogHelper.i(TAG,"------选中的spinner的id"+mCompanyId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.but_item_sure:
                //点击按钮提交数据

                postCompleteData();
                break;
            case R.id.but_item_cancel:
                //点击按钮取消

                break;
        }
    }
    /*
    *封装完善公司信息----数据提交  TODO
     */
    private void postCompleteData() {
        String mNewCompany=mCCName.getText().toString();
        String mMoney=mCMoney.getText().toString();
        String mAddress=mJYAddress.getText().toString();
        String mScope=mJYFanWei.getText().toString();
        String mCompanyType=mCompanyNames[mCompanyId];

        JSONObject mJSON=new JSONObject();
        //mJSON.put("userId",)
        JSONObject mJSONComplete=new JSONObject();
        JSONArray mJSONContacts=new JSONArray();
        try {
            if (TextUtils.isEmpty(mNewCompany)) {
                App.getInstance().showToast("请完善信息");
                return;
            } else {
                mJSONComplete.put("CompanyName", mNewCompany);
            }
            if (TextUtils.isEmpty(mMoney)) {
                App.getInstance().showToast("请完善信息");
                return;
            } else {
                mJSONComplete.put("Register", mMoney);
            }
            if (TextUtils.isEmpty(mAddress)) {
                App.getInstance().showToast("请完善信息");
                return;
            } else {
                mJSONComplete.put("BusinessAddress", mAddress);
            }
            if (TextUtils.isEmpty(mScope)) {
                App.getInstance().showToast("请完善信息");
                return;
            } else {
                mJSONComplete.put("ScopeAddress", mScope);
            }
            if (mListData.size()<=0){
                App.getInstance().showToast("请添加公司人员");
                return;
            }
            mJSONComplete.put("CompanyType", mCompanyType);
            for (CompleteJavaBean temp:mListData){
                JSONObject mCompleteItem=new JSONObject();
                mCompleteItem.put("name",temp.getmName());//姓名
                mCompleteItem.put("ratio",temp.getmBiLi());//比例
                mCompleteItem.put("sfznumber",temp.getmNumber());//身份证号码
                mCompleteItem.put("sfzaddress",temp.getmAddress());//身份证地址
                mJSONContacts.put(mCompleteItem);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            mJSON.putOpt("CompleteMess",mJSONComplete);
            mJSON.putOpt("CompleteContacts",mJSONContacts);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogHelper.i(TAG, "---修改的的所有信息" + mJSON.toString());
        createProgressDialog("正在完善信息...");
        //完善信息---提交
        VolleyHelpApi.getInstance().postCompleteMessage(mUId, mJSON, new APIListener() {
            @Override
            public void onResult(Object result) {
                dismissProgressDialog();
                LogHelper.i(TAG,"完善信息成功");
                CompleteMessage.this.finish();
            }
            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });
    }
    /**
     * 创建对话框
     *
     * @param title
     */
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
     * listview适配器
     */
    class CompleteAdapter extends ArrayAdapter<CompleteJavaBean>  {
        public CompleteAdapter(ArrayList<CompleteJavaBean> item) {
            super(CompleteMessage.this, 0, item);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView==null){
                holder=new ViewHolder();
                convertView=View.inflate(CompleteMessage.this,R.layout.complete_item_list,null);
                holder.mItemName= (TextView) convertView.findViewById(R.id.item_name);
                holder.mItemBiLi= (TextView) convertView.findViewById(R.id.item_bili);
                holder.mItemNumber= (TextView) convertView.findViewById(R.id.item_zhenjian_number);
                holder.mItemAddress= (TextView) convertView.findViewById(R.id.item_zhenjian_address);

                holder.mItemWork= (TextView) convertView.findViewById(R.id.item_work_zhiwei);
                holder.mItemAddRen= (Button) convertView.findViewById(R.id.but_item_addren);
                holder.mItemCancelRen= (Button) convertView.findViewById(R.id.but_item_cancelren);
                holder.mItemSure= (Button) convertView.findViewById(R.id.but_item_sure);
                holder.mItemCancel= (Button) convertView.findViewById(R.id.but_item_cancel);

                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
          CompleteJavaBean item=getItem(position);
            holder.mItemName.setText(item.getmName());
            holder.mItemBiLi.setText(item.getmBiLi());
            holder.mItemNumber.setText(item.getmNumber());
            holder.mItemAddress.setText(item.getmAddress());
            holder.mItemWork.setText(item.getmPWork());

           /* if (position == mListData.size() - 1) {
                holder.mItemCancelRen.setVisibility(View.VISIBLE);
            } else{
                holder.mItemCancelRen.setVisibility(View.GONE);
            }*/

            //添加人员
            holder.mItemAddRen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  mListData.add(new CompleteJavaBean());
                  adapter.notifyDataSetChanged();
                }
            });

            //取消人员
            holder.mItemCancelRen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteOneItem(position);
                }
            });
            return convertView;
        }
    }
    private void deleteOneItem(int position) {
        if (mListData.size() > 2) {
            mListData.remove(position);
            adapter.notifyDataSetChanged();
        } else {
            App.getInstance().showToast("最后两个必须填写了");
        }
    }
    static class ViewHolder{
        TextView mItemName;
        TextView mItemBiLi;
        TextView mItemNumber;
        TextView mItemAddress;
        TextView mItemWork;
        Button mItemAddRen;
        Button mItemCancelRen;
        Button mItemSure;
        Button mItemCancel;
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
