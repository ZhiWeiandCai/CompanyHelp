package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * author :an
 */
public class NoPayFragment extends Fragment {

    private static final String TAG = "NoPayFragment";
    private MyOrderActivity mMyOrderActivity;
    private int uid;
    private MyNoPayOrderAdapter mMyNoPayOrderAdapter;
    private ListView mListNoPay;

    private ArrayList<NoPayBean> mNoPayList=new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMyOrderActivity= (MyOrderActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_no_pay, container, false);
        mListNoPay = (ListView) view.findViewById(R.id.list_no_pay);


        Bundle arguments = getArguments();
        if (arguments!=null) {
            uid = arguments.getInt("uid");
            LogHelper.i(TAG, "----nopay--" + uid);

            getNoPayOrder(uid);



        }
        return view;
    }


    private void setadapterData() {

        mMyNoPayOrderAdapter = new MyNoPayOrderAdapter();
        mListNoPay.setAdapter(mMyNoPayOrderAdapter);
    }

    //请求未支付的订单
    private void getNoPayOrder(int uid) {
        /**
         * 根据用户id获取未支付订单
         */
            VolleyHelpApi.getInstance().getNoPayYeWu(uid, new APIListener() {
                @Override
                public void onResult(Object result) {
                    LogHelper.i(TAG, "----未支付订单的所有信息---");
                    JSONObject jsonObject= (JSONObject) result;
                    LogHelper.i(TAG, "----未支付订单的所有信息---"+jsonObject.toString());

                    JSONArray mNoPayJA;
                    mNoPayJA = ((JSONObject) result).optJSONArray("entity");
                    int compJALength = mNoPayJA.length();
                    LogHelper.i(TAG,"-----"+compJALength);

                    /**
                     * [{"businezzType":"10","hasAccount":"N","placeOrderTime":"2016-09-24 15:37:18","orderid":6,"orderName":"注册公司","orderFee":"3"},
                     * {"businezzType":"10","placeOrderTime":"2016-09-24 15:37:25","orderid":7,"orderName":"注册公司","orderFee":"3"}]
                     *
                     */
                    for (int i = 0; i < compJALength; i++) {
                        JSONObject temp = mNoPayJA.optJSONObject(i);
                        LogHelper.i(TAG,i+"-----"+compJALength);

                        NoPayBean iten=new NoPayBean();

                        String businezzType = temp.optString("businezzType");
                        String hasAccount = temp.optString("hasAccount");
                        String placeOrderTime = temp.optString("placeOrderTime");
                        String orderid = temp.optString("orderid");
                        String orderName = temp.optString("orderName");

                        iten.setHasAccount(hasAccount);
                        String orderFee = temp.optString("orderFee");

                        iten.setBusinezzType(businezzType);
                        iten.setPlaceOrderTime(placeOrderTime);
                        iten.setOrderid(orderid);
                        iten.setOrderName(orderName);
                        iten.setOrderFee(orderFee);

                        LogHelper.i(TAG,"----订单id---"+orderid+"hasAccount="+hasAccount+"businezzType=:"+businezzType+"-placeOrderTime="+placeOrderTime+"--orderName"+orderName+"-orderFee"+orderFee);
                        mNoPayList.add(iten);
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



    public class MyNoPayOrderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mNoPayList.size()!=0){
                return  mNoPayList.size();
            }
            return 0;
        }
        @Override
        public Object getItem(int position) {
            return mNoPayList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                holder=new ViewHolder();
                convertView=View.inflate(mMyOrderActivity,R.layout.no_pay_item,null);


                holder.mTime= (TextView) convertView.findViewById(R.id.item_nopay_time);
                holder.mTitle= (TextView) convertView.findViewById(R.id.item_nopay_title);
                holder.mMoney= (TextView) convertView.findViewById(R.id.item_nopay_money);
                holder.mButCancel= (Button) convertView.findViewById(R.id.item_nopay_cancel);
                holder.mButComplete= (Button) convertView.findViewById(R.id.item_nopay_complete);
                holder.mTitle1= (TextView) convertView.findViewById(R.id.item_nopay_title1);
                holder.mImage1= (ImageView) convertView.findViewById(R.id.item_nopay_img1);
                holder.mImage= (ImageView) convertView.findViewById(R.id.item_nopay_img);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }

            holder.mButComplete.setTextColor(Color.WHITE);
            final NoPayBean item=mNoPayList.get(position);
            holder.mTime.setText(item.getPlaceOrderTime());
            String orderName = item.getOrderName();
            holder.mTitle.setText(orderName);

            String orderFee = item.getOrderFee();
            int money = Integer.parseInt(orderFee);
            holder.mMoney.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen),money /100.0f));

            String hasAccount = item.getHasAccount();

            if ("Y".equals(hasAccount)) {
                LogHelper.i(TAG,"-------------66---------------hasAccount--"+hasAccount);
                holder.mTitle1.setText("代理记账");
                holder.mImage1.setVisibility(View.VISIBLE);
                holder.mImage1.setBackgroundResource(R.mipmap.ji_bao);
            } else {
                holder.mTitle1.setText("");
                holder.mImage1.setVisibility(View.GONE);
            }



            //添加不同业务的图标
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

            holder.mButComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mMyOrderActivity,PayOptActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("shangpin",item.getOrderName());
                    bundle.putString("weizhifu","weizhifu");

                    LogHelper.i(TAG,"----订单id-1--"+item.getOrderid());
                    bundle.putString("bookListId",item.getOrderid());
                    bundle.putFloat("pay_money",Integer.parseInt(item.getOrderFee())/100.0f);
                    bundle.putInt("position",position);
                    intent.putExtra("booklistdata",bundle);
                    startActivity(intent);

                }
            });
            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==10){
            String position = data.getStringExtra("position");
            mNoPayList.remove(position);
            LogHelper.i(TAG,"----huichuang-----");
            mMyNoPayOrderAdapter.notifyDataSetChanged();

        }
    }
    static class ViewHolder{
        TextView mTime;
        TextView mTitle;
        TextView mTitle1;
        TextView mMoney;
        ImageView mImage1;
        ImageView mImage;
        Button mButCancel;
        Button mButComplete;
    }

}
