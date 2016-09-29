package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.xht.android.companyhelp.model.PersonOfBianGengService;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/24.
 * author: an
 */
public class BianGengService extends Activity implements View.OnClickListener {

    private static final String TAG = "BianGengService";

    private int mUid;//用户id
    private EditText mEditCompany;//输入公司名称
    private Spinner mSpinnerCompany;//可选公司名称  // TODO
    private CheckBox mCheckBoxChangemCompany;//变更名称
    private EditText mEditNewCompany;//输入新的公司名称

    private CheckBox mCheckBoxChangeAddress;//勾选变更地址
    private CheckBox mCheckBoxKuaQuYU;//勾选跨区域
    private EditText mEditNewCompanyAddress;//输入新的公司地址

    private CheckBox mCheckBoxChangeFaRen;//勾选变更法人
    private EditText mEditNewFaRenName;//新的法人名称
    private EditText mEditNewShengFengZhengHao;//新法人身份证号

    private CheckBox mCheckBoxChangeFanWei;//勾选变更经营范围
    private EditText mEditNewFanWei;//输入新的经营范围

    private CheckBox mCheckBoxChangeGuDong;//勾选变更股东股权
    private ImageView mImageContacts;//添加联系人的按钮

    private TextView mTVMoney; //总价钱
    private Button mSendMoney;//下单按钮

    private ListView mListViewItem;//添加item



    private ProgressDialog mProgDoal;//对话框


    private int mPrice;//总价钱
    private int mNamePrice;//变更名称价钱
    private int mFaRenPrice;//变更法人价钱
    private int mAddressPrice;//变更地址价钱
    private int mFanWeiPrice;//变更范围价钱
    private int mGuDongGuQuanPrice;//变更股东股权价钱

    private String[] mCompNames;//公司名字数组
    private int[] mCompIds;
    private int mSelectedCompId;//选中公司的ID

    //变更业务
    private String mBGCompanyName;
    private String mBGCompanyAddress;
    private String mBGFaRenName;
    private String mBGFaRenSFZHao;
    private String mBGJYFanWei;
    private String mBGGuDGuQ;

    private boolean mCheckKuaQuYU;

    private int flag=0;

    private View view;

    private String companyName;//输入或选择的公司名字
    private ArrayList<PersonOfBianGengService> mArrayList = new ArrayList<>();//存储变更股东股权的信息
    private PersonOfBianGengServiceAdapter mPersonOfBianGengAdapter;//每个item的信息
    //private boolean mCheckBoxGuDong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_biangeng);
        Bundle bundle = getIntent().getBundleExtra("uData");
        mUid = bundle.getInt("uid");//下单人ID
        LogHelper.i(TAG,"下单人ID---"+mUid);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("下单预约-变更服务");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);


        //mArrayList.add(new PersonOfBianGengService());
        //初始化变更界面主布局
        initView();
        //添加子listview的头
        view=View.inflate(BianGengService.this,R.layout.header_listview,null);
        //初始化listview的头布局
        initViewFind();

        mListViewItem.addHeaderView(view);

        //给listview设置适配器
        mPersonOfBianGengAdapter = new PersonOfBianGengServiceAdapter(mArrayList);
        mListViewItem.setAdapter( mPersonOfBianGengAdapter);
        //listview 条目被点击时触发
        mListViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        LogHelper.i(TAG,"-----点击了条目");
                        Intent intent=new Intent(BianGengService.this,PersonOfBianGengAvtivity.class);

                        intent.putExtra("mGuDongGuQuan", mArrayList.get(position-1).getmGuDongGuQuan());
                        intent.putExtra("mGuQuanZenJian", mArrayList.get(position-1).getmGuDGuQZengJian());
                        intent.putExtra("mName", mArrayList.get(position-1).getName());
                        intent.putExtra("mShengFengZheng", mArrayList.get(position-1).getNumberOfShengfenzheng());
                        intent.putExtra("mSFZAddress", mArrayList.get(position-1).getAddress());
                        intent.putExtra("mGuQuangBiLi", mArrayList.get(position-1).getGuquanbili()+"");

                        intent.putExtra("whichItem", position - 1);

               BianGengService.this.startActivityForResult(intent,0);
            }
        });

        //根据用户ID获取公司名称
        getComListZhuCe(mUid);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        flag=1;
        if(requestCode==0){
            if(resultCode==-1){
                Bundle Data = data.getBundleExtra("biangengServiceData");
                int which = Data.getInt("mItem");
                mArrayList.get(which).setmGuDongGuQuan(Data.getString("bgGudongGuquan"));
                mArrayList.get(which).setmGuDGuQZengJian(Data.getString("zjGudongGuquan"));
                mArrayList.get(which).setName(Data.getString("name"));
                mArrayList.get(which).setNumberOfShengfenzheng(Data.getString("sfzHao"));
                mArrayList.get(which).setAddress(Data.getString("sfzAddress"));
                mArrayList.get(which).setGuquanbili(Data.getInt("guquanBili"));
                mPersonOfBianGengAdapter.notifyDataSetChanged();

               LogHelper.i(TAG,"-------"+Data.toString()+"-------------------------");

            }
        }
    }

    private void initViewFind() {
        //变更业务
        mEditCompany= (EditText) view.findViewById(R.id.biangeng_edit_company);
        mSpinnerCompany= (Spinner) view.findViewById(R.id.company_name_spinner);

        //变更名称
        mCheckBoxChangemCompany= (CheckBox) view.findViewById(R.id.biangeng_check_company_name);
        mEditNewCompany= (EditText) view.findViewById(R.id.biangeng_edit_new_company);
//TODO
        mCheckBoxChangemCompany.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mPrice+=mNamePrice;

                }else{
                    mPrice-=mNamePrice;
                }
                mTVMoney.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice/100.0f));

            }
        });

        //变更地址
        mCheckBoxChangeAddress= (CheckBox) view.findViewById(R.id.biangeng_checkbox_address);
        mCheckBoxKuaQuYU= (CheckBox) view.findViewById(R.id.biangeng_kuaqu_checkbox);
        mEditNewCompanyAddress= (EditText) view.findViewById(R.id.biangeng_new_company_address);
        mCheckBoxChangeAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mPrice+=mAddressPrice;

                }else{
                    mPrice-=mAddressPrice;
                }
                mTVMoney.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice/100.0f));
            }
        });

        //变更法人
        mCheckBoxChangeFaRen= (CheckBox) view.findViewById(R.id.biangeng_new_faren_checkbox);
        mEditNewFaRenName = (EditText) view.findViewById(R.id.biangeng_edit_new_faren_name);
        mEditNewShengFengZhengHao= (EditText) view.findViewById(R.id.biangeng_new_shengfengzhenghao_edit);
        mCheckBoxChangeFaRen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mPrice+=mFaRenPrice;

                }else{
                    mPrice-=mFaRenPrice;
                }
                mTVMoney.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice/100.0f));
            }
        });

        //变更经营范围
        mCheckBoxChangeFanWei= (CheckBox) view.findViewById(R.id.biangeng_check_jingying_fanwei);
        mEditNewFanWei= (EditText) view.findViewById(R.id.biangeng_new_jingying_fanwei);
        mCheckBoxChangeFanWei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mPrice+=mFanWeiPrice;

                }else{
                    mPrice-=mFanWeiPrice;
                }
                mTVMoney.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice/100.0f));
            }
        });

        //变更股东股权
        mCheckBoxChangeGuDong= (CheckBox) view.findViewById(R.id.biangeng_new_gudong_guquan);
        mCheckBoxChangeGuDong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mPrice+=mGuDongGuQuanPrice;
                    mArrayList.add(new PersonOfBianGengService());

                }else{
                    mPrice-=mGuDongGuQuanPrice;
                    //mArrayList.remove(new PersonOfBianGengService());
                    mArrayList.removeAll(mArrayList);
                }
                mPersonOfBianGengAdapter.notifyDataSetChanged();
                mTVMoney.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice/100.0f));
            }
        });
        mTVMoney.setTextColor(Color.RED);

        //点击动态添加ListView的item
        //mImageContacts.setOnClickListener(this);

       mSpinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogHelper.i("单击了某些选项", "position=" + position + "--gongsi=" + mCompNames[position]);
                mSelectedCompId = mCompIds[position];
                companyName=mCompNames[position];
                mEditCompany.setText(mCompNames[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void initView() {

        //价钱文本和下单按钮
        mTVMoney= (TextView) findViewById(R.id.zong_money);
        mSendMoney= (Button) findViewById(R.id.sendbook_biangeng);
        //listview
        mListViewItem= (ListView) findViewById(R.id.biangeng_list_view);
        mSendMoney.setOnClickListener(this);
    }

//适配器
    private class PersonOfBianGengServiceAdapter extends  ArrayAdapter<PersonOfBianGengService> {

        public PersonOfBianGengServiceAdapter(ArrayList<PersonOfBianGengService> item) {
            super(BianGengService.this, 0, item);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
             ViewHolder holder;
           if (convertView==null){
               holder = new ViewHolder();
               convertView=View.inflate(BianGengService.this,R.layout.person_item_gudongguquan,null);
               holder.mGuDongGuQuan= (TextView) convertView.findViewById(R.id.item_biangeng_gudguq);
               holder.mGuQuanZenJian= (TextView)convertView.findViewById(R.id.item_biangeng_zengjian);
               holder.mName= (TextView) convertView.findViewById(R.id.item_biangeng_list_name);
               holder.mShengFengZheng= (TextView) convertView.findViewById(R.id.item_shengfeng_zhenghao_text);
               holder.mJianImgContacts= (ImageButton) convertView.findViewById(R.id.item_biangeng_list_button);
               holder.mjiaImgContacts= (ImageButton) convertView.findViewById(R.id.biangeng_add_contacts);
               holder.mSFZAddress= (TextView) convertView.findViewById(R.id.item_biangeng_shenfz_address);
               holder.mGuQuangBiLi= (TextView)convertView.findViewById(R.id.item_biangeng_text_guquan_bili);
               convertView.setTag(holder);
           }else{
               holder= (ViewHolder) convertView.getTag();
           }
            LogHelper.i(TAG,"---------进入到item条目中去了-----");

            PersonOfBianGengService item = getItem(position);

            holder.mGuDongGuQuan.setText(item.getmGuDongGuQuan());
            holder.mGuQuanZenJian.setText(item.getmGuDGuQZengJian());
            holder.mName.setText(item.getName());
            holder.mShengFengZheng.setText(item.getNumberOfShengfenzheng());
            holder.mGuQuangBiLi.setText(item.getGuquanbili()+" ");
            holder.mSFZAddress.setText(item.getAddress());




            holder.mJianImgContacts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteOneItem(position);
                }
            });

            holder.mjiaImgContacts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addNewOneItem(position);

                }
            });
            return convertView;

        }

    }

    private void addNewOneItem(int position) {
        boolean  mCheckBoxGuDong = mCheckBoxChangeGuDong.isChecked();
        if (mCheckBoxGuDong) {
            //添加股东股权
            if (mArrayList.size() < 10) {
                mArrayList.add(new PersonOfBianGengService());
                App.getInstance().showToast("已添加一项...");
                mPersonOfBianGengAdapter.notifyDataSetChanged();
            } else {
                App.getInstance().showToast("一次最多只能增减10个股东或股权");
            }

        }else{
            App.getInstance().showToast("请选勾选变更股东股权");
        }

    }

    static class ViewHolder{
       TextView mGuDongGuQuan;
       TextView mGuQuanZenJian;
        TextView mName;
       TextView mShengFengZheng;
        TextView mSFZAddress;
        ImageButton mJianImgContacts;
        ImageButton mjiaImgContacts;
        TextView mGuQuangBiLi;
        int position;
    }
    private void deleteOneItem(int position) {
        if (mArrayList.size() > 0) {
            mArrayList.remove(position);
            mPersonOfBianGengAdapter.notifyDataSetChanged();
        } else {
            App.getInstance().showToast("取消最后一个变动了");
        }
    }


    /**
     * {"price":{"ChgLegalPerson":1,"ChgGuDGuQ":1,"ChgBusinessAddr":1
     *            ,"ChgCompName":1,"ChgBusinessScope":1}
     * 根据用户id获取公司列表和变更业务价钱
     */
    private void getComListZhuCe(int uid) {
        createProgressDialog("价格获取中...");

        LogHelper.i(TAG,"变更服务---------------");
        VolleyHelpApi.getInstance().getBianGengComListYeWu(uid, new APIListener() {
            @Override
            public void onResult(Object result) {
                try {
                    JSONObject  jiageJO ;
                    JSONArray companyJA;
                    /*private int mPrice;//总价钱
                    private int mNamePrice;//变更名称价钱
                    private int mFaRenPrice;//变更法人价钱
                    private int mAddressPrice;//变更地址价钱
                    private int mFanWeiPrice;//变更范围价钱
                    private int mGuDongGuQuanPrice;//变更股东股权价钱*/
                    try{
                    jiageJO = ((JSONObject) result).getJSONObject("price");
                   // int jiageJoLength=jiageJO.length();
                        mFaRenPrice = jiageJO.optInt("ChgLegalPerson");
                        mGuDongGuQuanPrice = jiageJO.optInt("ChgGuDGuQ");
                        mAddressPrice = jiageJO.optInt("ChgBusinessAddr");
                        mNamePrice = jiageJO.optInt("ChgCompName");
                        mFanWeiPrice = jiageJO.optInt("ChgBusinessScope");
                    LogHelper.i(TAG,"变更服务价格的所有信息"+jiageJO.toString()+"::::::::::::"+mFaRenPrice+mGuDongGuQuanPrice+mAddressPrice+mNamePrice+mFanWeiPrice+"----------------");
                  //  companyJA = ((JSONObject) result).getJSONArray("companyName");
                        companyJA = ((JSONObject) result).optJSONArray("companyName");
                    int compJALength = companyJA.length();
                    mCompIds = new int[compJALength];
                    mCompNames = new String[compJALength];
                    for (int i = 0; i < compJALength; i++) {
                        JSONObject temp = companyJA.optJSONObject(i);
                        mCompIds[i] = temp.optInt("id");
                        mCompNames[i] = temp.optString("name");
                    }

                        companyName=mCompNames[0];
                        LogHelper.i(TAG,"------------------------"+companyName+"---------------");
                } catch (JSONException e) {
                    e.printStackTrace();
                    }
                    dismissProgressDialog();
                    mSelectedCompId = mCompIds[0];
                    refleshCompanyView();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
                finish();
            }
        });
    }
    private void refleshCompanyView() {
        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<CharSequence>(BianGengService.this, android.R.layout.simple_spinner_item, mCompNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCompany.setAdapter(arrayAdapter);
        //mTVTotalMoney.setText(mPrice);
        mTVMoney.setTextColor(Color.RED);
        mTVMoney.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice/100.0f));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sendbook_biangeng:
                //检查是否填写信息
                checkInFo();
                break;
        }
    }
    private void checkInFo() {

        for (PersonOfBianGengService temp:mArrayList){
            if (temp.getName()==null || temp.getName()==""){
                App.getInstance().showToast("请填写信息再提交");
                return;
            }
        }
        //提交订单
        postBookList();
    }

    /**
     * 获取信息提交订单 TODO
     */
    private void postBookList() {
        boolean mCheckBoxCompany=mCheckBoxChangemCompany.isChecked();
        if (mCheckBoxCompany) {
            //变更公司名称
            mBGCompanyName=mEditNewCompany.getText().toString();
            if(TextUtils.isEmpty(mBGCompanyName)){
                App.getInstance().showToast("请输入变更后的公司名称");
                return;
            }
        }
        boolean mCheckBoxAddress=  mCheckBoxChangeAddress.isChecked();
        if (mCheckBoxAddress) {
         //   mPrice+=mAddressPrice;
            //跨区域
            mCheckKuaQuYU= mCheckBoxKuaQuYU.isChecked();
            //变更公司地址
            mBGCompanyAddress=mEditNewCompanyAddress.getText().toString();
            if(TextUtils.isEmpty(mBGCompanyAddress)){
                App.getInstance().showToast("请输入变更后的公司详细地址");
                return;
            }

        }

        boolean mCheckBoXFaren=mCheckBoxChangeFaRen.isChecked();
        if (mCheckBoXFaren) {
          //  mPrice+=mFaRenPrice;
            //变更法人和身份证号
            mBGFaRenName=mEditNewFaRenName.getText().toString();
             mBGFaRenSFZHao=mEditNewShengFengZhengHao.getText().toString();
            if(TextUtils.isEmpty(mBGFaRenName)){
                App.getInstance().showToast("请输入变更后的法人姓名");
                return;
            }
            if(TextUtils.isEmpty(mBGFaRenSFZHao)){
                App.getInstance().showToast("请输入变更后的法人身份证号码");
                return;
            }
        }

        boolean mCheckBoxFanWei=mCheckBoxChangeFanWei.isChecked();
        if (mCheckBoxFanWei) {
           // mPrice+=mFanWeiPrice;
            //变更范围
            mBGJYFanWei=mEditNewFanWei.getText().toString();
            if(TextUtils.isEmpty(mBGJYFanWei)){
                App.getInstance().showToast("请输入变更后的经营范围");
                return;
            }
        }


        boolean  mCheckBoxGuDong = mCheckBoxChangeGuDong.isChecked();
        if (mCheckBoxGuDong){
            if (mArrayList.size()<=0){
                App.getInstance().showToast("请添加变更股东股权的相关信息");
                return;
            }
        }

      /*  if (mCheckBoxGuDong) {
            //添加股东股权
            App.getInstance().showToast("请添加变更股东股权的相关信息");

        }*/
        if (!mCheckBoxCompany&&!mCheckBoxAddress&&!mCheckBoXFaren&&!mCheckBoxFanWei&&!mCheckBoxGuDong){
            App.getInstance().showToast("变更业务请先勾选类型，若无请退出！");
            return;
        }

        //封装数据
        JSONObject mBG=new JSONObject();

        try {
            mBG.put("CustomerId",mUid);//下单用户id
            // mBG.put("CompanyId",mSelectedCompId);//公司id

            mBG.put("CompanyName",companyName);
            mBG.put("Price",mPrice);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //变更名称
        try {
            JSONObject mBGMCYeWu = new JSONObject();

            mBGMCYeWu.put("bgcheckcompany", mCheckBoxCompany);
            mBGMCYeWu.put("bgnewcompany",mBGCompanyName);
            mBGMCYeWu.opt("biangengmingchen");
            mBG.putOpt("bgmc",mBGMCYeWu);
           // LogHelper.i(TAG,"--------"+mBGMCYeWu.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        //变更地址
        try {
            JSONObject mBGDZYeWu = new JSONObject();
            mBGDZYeWu.put("bgcheckcompanyaddress", mCheckBoxAddress);
            mBGDZYeWu.put("bgcheckaddresskuaqu", mCheckKuaQuYU);
            mBGDZYeWu.put("bgnewcompanyaddress",mBGCompanyAddress);
            mBG.putOpt("bgdz",mBGDZYeWu);
          //  LogHelper.i(TAG,"--------"+mBGDZYeWu.toString());
        }catch (Exception e){
            e.printStackTrace();
        }



       //变更法人
        try {
            JSONObject mBGFRYeWu = new JSONObject();
            mBGFRYeWu.put("bgcheckfaren", mCheckBoXFaren);
            mBGFRYeWu.put("bgnewfaren",mBGFaRenName);
            mBGFRYeWu.put("bgnewfarensfz",mBGFaRenSFZHao);
            mBG.putOpt("bgfr",mBGFRYeWu);
            LogHelper.i(TAG,mBGFRYeWu.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        //变更经营范围
        try {
            JSONObject mBGJYYeWu = new JSONObject();
            mBGJYYeWu.put("bgcheckfanwei", mCheckBoxFanWei);
            mBGJYYeWu.put("bgnewfanwei",mBGJYFanWei);
            mBG.putOpt("bgfw",mBGJYYeWu);
            LogHelper.i(TAG,mBGJYYeWu.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        //变更股东股权
        try {
            JSONArray mBGList=new JSONArray();
            JSONObject mBGoject = new JSONObject();
            mBGoject.put("biangengguquan", mCheckBoxGuDong);
            for (PersonOfBianGengService temp : mArrayList) {
                JSONObject mBianGengJo = new JSONObject();
                mBianGengJo.put("bggudongguquan", temp.getmGuDongGuQuan());
                mBianGengJo.put("bgzengjian", temp.getmGuDGuQZengJian());
                mBianGengJo.put("bgname", temp.getName());
                mBianGengJo.put("bgsfzhao", temp.getNumberOfShengfenzheng());
                mBianGengJo.put("bgaddress", temp.getAddress());
                mBianGengJo.put("bgguquanbili", temp.getGuquanbili());

                mBGList.put(mBianGengJo);

            }

            mBG.putOpt("bggq",mBGList);
        }catch (Exception e){
            e.printStackTrace();
        }

        mTVMoney.setTextColor(Color.RED);
        mTVMoney.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice/100.0f));
        createProgressDialog("订单提交中...");
        LogHelper.i(TAG, "----提交的所有信息" + mBG.toString());

        createProgressDialog("订单提交中...");
        //变更服务提交订单
        VolleyHelpApi.getInstance().postDingDanBianGengService(mUid, mBG, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i("订单提交成功", "2016-08-29");
                dismissProgressDialog();
                Bundle bundle = new Bundle();
                JSONObject tempJO = ((JSONObject) result).optJSONObject("entity");
                bundle.putString("shangpin", "变更服务");
                bundle.putString("bookListId", tempJO.optString("orderid"));
                bundle.putFloat("pay_money", mPrice/100.0f);
                Intent intent = new Intent(BianGengService.this, PayOptActivity.class);
                intent.putExtra("booklistdata", bundle);
                BianGengService.this.startActivity(intent);
                BianGengService.this.finish();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });

        LogHelper.i(TAG,"---"+mCheckBoxCompany+"---"+mCheckBoxAddress+"--"+mCheckBoXFaren+"--"+mCheckBoxFanWei+"--"+ mCheckBoxGuDong +"--");
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
