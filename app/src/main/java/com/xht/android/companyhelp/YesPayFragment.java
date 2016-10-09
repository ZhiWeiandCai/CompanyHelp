package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xht.android.companyhelp.model.NoPayBean;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-23.
 *  author :an
 */
public class YesPayFragment extends Fragment {

    private int uid;
    private static final String TAG = "YesPayFragment";

    private MyOrderActivity mMyOrderActivity;
    private MyYesPayOrderAdapter mMyYesPayOrderAdapter;
    private ListView mListYesPay;
    private ArrayList<NoPayBean> mYesPayList=new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMyOrderActivity= (MyOrderActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yes_pay, container, false);
        mListYesPay = (ListView) view.findViewById(R.id.list_yes_pay);
        Bundle arguments = getArguments();
        if (arguments!=null){
            uid = arguments.getInt("uid");
            LogHelper.i(TAG,"-yespay--"+ uid);
            getYesPayOrder(uid);



        }
        return view;
    }



    private void setadapterData() {

        mMyYesPayOrderAdapter = new MyYesPayOrderAdapter();
        mListYesPay.setAdapter(mMyYesPayOrderAdapter);
    }

    //请求支付的订单
    private void getYesPayOrder(int uid) {
        /**
         * 根据用户id获取支付订单
         */
        VolleyHelpApi.getInstance().getYesPayYeWu(uid, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i(TAG, "----支付订单的所有信息---");
                JSONObject jsonObject= (JSONObject) result;
                LogHelper.i(TAG, "----支付订单的所有信息---"+jsonObject.toString());

                JSONArray mNoPayJA;
                mNoPayJA = ((JSONObject) result).optJSONArray("entity");
                int compJALength = mNoPayJA.length();
                LogHelper.i(TAG,"-----"+compJALength);

                /**
                 * [{"businezzType":"10","hasAccount":"N","placeOrderTime":"2016-09-24 15:37:18","orderid":6,"orderName":"注册公司","orderFee":"3"},
                 * {"businezzType":"10","hasAccount":"N","placeOrderTime":"2016-09-24 15:37:25","orderid":7,"orderName":"注册公司","orderFee":"3"}]
                 *
                 *
                 */
                for (int i = 0; i < compJALength; i++) {
                    JSONObject temp = mNoPayJA.optJSONObject(i);
                    LogHelper.i(TAG,i+"-----"+compJALength);

                    NoPayBean iten=new NoPayBean();
                    iten.setBusinezzType(temp.optString("businezzType"));
                    iten.setHasAccount(temp.optString("hasAccount"));
                    iten.setPlaceOrderTime(temp.optString("placeOrderTime"));
                    iten.setOrderid(temp.optString("orderid"));
                    iten.setOrderName(temp.optString("orderName"));
                    iten.setOrderFee(temp.optString("orderFee"));

                    mYesPayList.add(iten);
                }

                //设置数据
                setadapterData();

            }
            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
            }
        });
    }

    public class MyYesPayOrderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mYesPayList.size()!=0){
                return  mYesPayList.size();
            }
            return 0;

        }

        @Override
        public Object getItem(int position) {
            return mYesPayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                holder=new ViewHolder();
                convertView=View.inflate(mMyOrderActivity,R.layout.yes_pay_item,null);

                holder.mTime= (TextView) convertView.findViewById(R.id.item_yespay_time);
                holder.mTitle= (TextView) convertView.findViewById(R.id.item_yespay_title);
                holder.mMoney= (TextView) convertView.findViewById(R.id.item_yespay_money);
                holder.mButComplete= (Button) convertView.findViewById(R.id.item_yespay_complete);
                holder.mTitle1= (TextView) convertView.findViewById(R.id.item_yespay_title1);
                holder.mImage= (ImageView) convertView.findViewById(R.id.item_yespay_img);
                holder.mImage1= (ImageView) convertView.findViewById(R.id.item_yespay_img1);
                convertView.setTag(holder);
            }else{

                holder= (ViewHolder) convertView.getTag();
            }
            holder.mButComplete.setTextColor(Color.WHITE);
            NoPayBean item=mYesPayList.get(position);
            holder.mTime.setText(item.getPlaceOrderTime());

            String orderName = item.getOrderName();
            holder.mTitle.setText(orderName);
            String orderFee = item.getOrderFee();
           int money = Integer.parseInt(orderFee);
            holder.mMoney.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen),money /100.0f));
            String hasAccount = item.getHasAccount();
            if ("Y".equals(hasAccount)){

                holder.mTitle1.setText("代理记账");
                holder.mImage1.setVisibility(View.VISIBLE);
                holder.mImage1.setBackgroundResource(R.mipmap.ji_bao);
            }else{
                holder.mTitle1.setText("");
                holder.mImage1.setVisibility(View.GONE);
            }
            switch (orderName){
                case "注册公司":
                    holder.mImage.setBackgroundResource(R.mipmap.zhu_gong);
                    break;
                case "记账报税":
                    holder.mImage.setBackgroundResource(R.mipmap.ji_bao);
                    break;
                case "社保服务":
                    holder.mImage.setBackgroundResource(R.mipmap.shebao);
                    break;
                case "发票服务":
                    holder.mImage.setBackgroundResource(R.mipmap.fa_fu);
                    break;
                case "注册商标":
                    holder.mImage.setBackgroundResource(R.mipmap.zhu_shang);
                    break;
                case "雇主保险":
                    holder.mImage.setBackgroundResource(R.mipmap.gu_bao);
                    break;
                case "变更服务":
                    holder.mImage.setBackgroundResource(R.mipmap.biao_fu);
                    break;
                case "注册资金":
                    holder.mImage.setBackgroundResource(R.mipmap.zhu_zi);
                    break;
                case "注销服务":
                    holder.mImage.setBackgroundResource(R.mipmap.zhux_fuw);
                    break;
                case "劳务派遣":
                    holder.mImage.setBackgroundResource(R.mipmap.lao_pai);
                    break;
            }
            return convertView;
        }
    }

    static class ViewHolder{
        TextView mTime;
        TextView mTitle;
        TextView mTitle1;
        TextView mMoney;
        ImageView mImage;
        ImageView mImage1;
        Button mButComplete;
    }




}
