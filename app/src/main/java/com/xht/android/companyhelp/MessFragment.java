package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xht.android.companyhelp.model.MessageDetail;
import com.xht.android.companyhelp.model.PayDetail;
import com.xht.android.companyhelp.model.UserInfo;
import com.xht.android.companyhelp.provider.MyDatabaseManager;
import com.xht.android.companyhelp.util.LogHelper;
import com.xht.android.companyhelp.util.Utils;

import java.util.ArrayList;

public class MessFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

	private MainActivity mActivity;//上下文
	private Button mZhiFuZhuShou;//按钮支付助手
	private Button mMessageCenter;//按钮消息中心
	private FrameLayout mFrameList;
	private ListView mListPay;//支付助手的listview
	private ListView mListMessage;//消息中心的listview
	private  UserInfo mUserInfo;
	private PayAdapter mPayAdapter;
	private MessageAdapter mMessageAdapter;
	private int mUid;

	private ArrayList<PayDetail> mPayList=new ArrayList();//订单支付成功发送的消息数据源

	private ArrayList<MessageDetail>mMessageList=App.getMessageList();//推送过来的消息的数据源

	public ArrayList<PayDetail> getPayList(){
		return mPayList;
	}
	private static final String TAG = "MessFragment";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPayAdapter=new PayAdapter();
		mMessageAdapter=new MessageAdapter(mMessageList);
		Bitmap mBitmap= BitmapFactory.decodeFile("http://photo.enterdesk.com/2008-11-23/200811221700467000.jpg");
		mUserInfo = ((MainActivity) mActivity).mUserInfo;
		//填充获取数据
		for (int i=0;i<10;i++){
			String time=Utils.getTimeUtils(System.currentTimeMillis());
			PayDetail itempay=new PayDetail();
			itempay.setmTime(time);
			itempay.setmTitle("注册资金");
			itempay.setmOrderNumber(123+i+"");
			itempay.setmTotalMoney(290*i+"");
			mPayList.add(itempay);


			//模拟数据报税请求 TODO
			/*MessageDetail itemMess=new MessageDetail();
			itemMess.setmTime(time);
			itemMess.setmTitle("报税请求");
			itemMess.setmContent(290*i+"");
			mMessageList.add(itemMess);*/
		}
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity= (MainActivity) activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isUserLogin()) {
			 mUid = mUserInfo.getUid();
			LogHelper.i(TAG,"----用户id=====-----"+mUid+"-------");
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mess, container, false);
		mZhiFuZhuShou = (Button) view.findViewById(R.id.but_zhufu);
		mMessageCenter = (Button) view.findViewById(R.id.but_message);
		mFrameList = (FrameLayout) view.findViewById(R.id.frage_list);

		mZhiFuZhuShou.setBackgroundColor(Color.TRANSPARENT);//默认选中支付助手
		//listview初始化
		mListPay= (ListView) view.findViewById(R.id.fram_listzhifu);
		mListMessage= (ListView) view.findViewById(R.id.fram_listmessage);

		mListPay.setAdapter(mPayAdapter);
		mListMessage.setAdapter(mMessageAdapter);

		//listview条目的点击事件
		mListPay.setOnItemClickListener(this);
		mZhiFuZhuShou.setOnClickListener(this);
		mMessageCenter.setOnClickListener(this);
		return view;
	}

	//listview支付助手的适配器 TODO 数据还没有获取
	class PayAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if (mPayList.size()>0){
				return mPayList.size();
			}
			return 0;
		}
		@Override
		public Object getItem(int position) {
			return mPayList.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolderPay mPayHolder;
			//复用历史对象
			if (convertView==null){
				mPayHolder=new ViewHolderPay();
				convertView=View.inflate(mActivity,R.layout.pay_list_item,null);
				mPayHolder.mTVTime= (TextView) convertView.findViewById(R.id.pay_current_time);
				mPayHolder.mTVMoney= (TextView) convertView.findViewById(R.id.pay_total_money);
				mPayHolder.mTVGoods= (TextView) convertView.findViewById(R.id.goods_detial);
				mPayHolder.mTVNumber= (TextView) convertView.findViewById(R.id.pay_number);
				convertView.setTag(mPayHolder);
			}else{
				mPayHolder= (ViewHolderPay) convertView.getTag();
			}
			//获取条目，给每个条目1填充数据
			PayDetail mPayItem=mPayList.get(position);
			mPayHolder.mTVTime.setText(mPayItem.getmTime());
			mPayHolder.mTVMoney.setText(mPayItem.getmTotalMoney()+"元");
			mPayHolder.mTVGoods.setText(mPayItem.getmTitle());
			mPayHolder.mTVNumber.setText(mPayItem.getmOrderNumber());

			return convertView;
		}
	}
	static class ViewHolderPay{
		TextView mTVTime;
		TextView mTVMoney;
		TextView mTVGoods;
		TextView mTVNumber;
	}




	//推送消息中心的适配器 TODO 推送的消息还没有获取
	class MessageAdapter extends ArrayAdapter<MessageDetail>{
		public MessageAdapter(ArrayList<MessageDetail> item) {
			super(mActivity, 0, item);
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolderMessage holderMessage;
			if (convertView==null){
				holderMessage=new ViewHolderMessage();
				convertView=View.inflate(mActivity,R.layout.message_list_item,null);
				holderMessage.mTitle= (TextView) convertView.findViewById(R.id.message_item_title);
				holderMessage.mTime= (TextView) convertView.findViewById(R.id.message_item_time);
					holderMessage.mIcom= (ImageView) convertView.findViewById(R.id.message_item_icom);
				holderMessage.mContent= (TextView) convertView.findViewById(R.id.message_item_content);

				convertView.setTag(holderMessage);
			}else{
				holderMessage= (ViewHolderMessage) convertView.getTag();
			}
			//填充每个item的数据
			final MessageDetail messItem=getItem(position);
			holderMessage.mTitle.setText(messItem.getmTitle());
			holderMessage.mTime.setText(messItem.getmTime());
			Bitmap bitmap=messItem.getmBitmap();
			if (bitmap==null){
				holderMessage.mIcom.setVisibility(View.GONE);
			}else{
				holderMessage.mIcom.setImageBitmap(messItem.getmBitmap());
			}

			mListMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String url=messItem.getmUrl();
					LogHelper.i(TAG,"-----"+url+"!!!!!!!!!!!!!!");

					if (TextUtils.isEmpty(url)){
						Intent intent=new Intent(getActivity(),MessageListActivity.class);
						LogHelper.i(TAG,"----==!!!!!!!!!!!!!!");
						startActivity(intent);
					}else{
						Intent intent=new Intent(getActivity(),UMessActivity.class);
						LogHelper.i(TAG,"----================");
						Bundle bundle=new Bundle();
						bundle.putString("url",url);
						intent.putExtra("listMess",bundle);
						startActivity(intent);
					}

				}
			});

			holderMessage.mContent.setText(messItem.getmContent());
			return convertView;
		}
	}
	static class ViewHolderMessage{
		TextView mTitle;
		TextView mContent;
		TextView mTime;
		ImageView mIcom;
	}
	/*private String mTitle;//推送通知的标题
	private String mContent;//推送通知的内容
	private String mTime;//记录通知到达的时间
	private String mUrl;//推送消息的url
	private String mUrlIcom;//推送过来的图标url*/

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.but_zhufu://点击支付助手
				mListPay.setVisibility(View.VISIBLE);
				mListMessage.setVisibility(View.GONE);
				mPayAdapter.notifyDataSetChanged();
				mMessageAdapter.notifyDataSetChanged();
				mZhiFuZhuShou.setBackgroundColor(Color.TRANSPARENT);
				mMessageCenter.setBackgroundColor(Color.GRAY);
				break;
			case R.id.but_message://点击消息中心
				mListPay.setVisibility(View.GONE);
				mListMessage.setVisibility(View.VISIBLE);
				mMessageAdapter.notifyDataSetChanged();
				mPayAdapter.notifyDataSetChanged();
				mZhiFuZhuShou.setBackgroundColor(Color.GRAY);
				mMessageCenter.setBackgroundColor(Color.TRANSPARENT);
				break;
		}
	}
	//listview的条目点击事件
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		switch(view.getId()){
			case R.id.but_zhufu:
				//点击跳转到支付详情
				break;
			case R.id.but_message:
				//点击跳转到信息页面
				break;
		}
	}
	boolean isUserLogin() {
		if (mUserInfo.getUid() == 0) {
			Cursor cursor = mActivity.getContentResolver().query(MyDatabaseManager.MyDbColumns.CONTENT_URI, null, null, null, null);
			if (cursor == null || cursor.getCount() == 0) {
				return false;
			}
			cursor.moveToFirst();
			int uidIndex = cursor.getColumnIndex(MyDatabaseManager.MyDbColumns.UID);
			int userNameIndex = cursor.getColumnIndex(MyDatabaseManager.MyDbColumns.NAME);
			int phoneIndex = cursor.getColumnIndex(MyDatabaseManager.MyDbColumns.PHONE);
			mUserInfo.setUid(cursor.getInt(uidIndex));
			mUserInfo.setUserName(cursor.getString(userNameIndex));
			mUserInfo.setPhoneNum(cursor.getLong(phoneIndex));
		}
		LogHelper.i(TAG, "mUserInfo.getUid() == " + mUserInfo.getUid() + "mUserInfo.getPhoneNum() == " + mUserInfo.getPhoneNum());
		return true;
	}

}
