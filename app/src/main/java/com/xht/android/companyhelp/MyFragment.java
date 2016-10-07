package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.message.tag.TagManager;
import com.xht.android.companyhelp.model.UserInfo;
import com.xht.android.companyhelp.provider.MyDatabaseManager;
import com.xht.android.companyhelp.util.AppInfoUtils;
import com.xht.android.companyhelp.util.LogHelper;

public class MyFragment extends Fragment implements OnClickListener {
	private static final String Tag = "MyFragment";
	public static final String BRO_ACT_S = "com.xht.android.companyhelp.bro_act_s";
	public static final String BRO_ACT_S2 = "com.xht.android.companyhelp.bro_act_s2";
	public static final String PHONENUM_KEY = "phone_key";
	public static final String UID_KEY = "userId_key";
	public static final String UNAME_KEY = "userName_key";
	private LinearLayout mLinearLayout1, mLinearLayout2, mLinearLayout3, mLinearLayout4,mLinearLayout5,mLinearLayout6,mLinearLayout7;
	private ImageView mHeadImageView;
	private MainActivity mActivity;
	private UserInfo mUserInfo;
	private TextView mPhoneNumView;
	private TextView mZhangHu;//账户管理
	private TextView mVersionDescTV;
	private static final String TAG = "MyFragment";

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			int uId = intent.getIntExtra(UID_KEY, 0);
			long phoneNum = intent.getLongExtra(PHONENUM_KEY, 0);
			String uName = intent.getStringExtra(UNAME_KEY);
			LogHelper.i(Tag, "uName=" + uName);
			mUserInfo.setUid(uId);
			mUserInfo.setPhoneNum(phoneNum);
			if (uName != null) {
				mUserInfo.setUserName(uName);
			}
			refleshUI();
		}
	};

	private BroadcastReceiver mClearUserReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			mUserInfo.setUid(0);
			mUserInfo.setPhoneNum(0);
			mUserInfo.setUserName(null);

			refleshUI2();
		}
	};
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter intentFilter = new IntentFilter(BRO_ACT_S);
		getActivity().registerReceiver(mReceiver, intentFilter);
		IntentFilter intentFilter2 = new IntentFilter(BRO_ACT_S2);
		getActivity().registerReceiver(mClearUserReceiver, intentFilter2);
		mUserInfo = ((MainActivity) mActivity).mUserInfo;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_my, container, false);
		mLinearLayout1 = (LinearLayout) view.findViewById(R.id.fragm_my_ll1);
		mLinearLayout2 = (LinearLayout) view.findViewById(R.id.fragm_my_ll2);
		mLinearLayout4 = (LinearLayout) view.findViewById(R.id.fragm_my_ll4);
		mLinearLayout3 = (LinearLayout) view.findViewById(R.id.fragm_my_ll3);
		mLinearLayout5 = (LinearLayout) view.findViewById(R.id.	fragm_my_ll5);
		//mLinearLayout6 = (LinearLayout) view.findViewById(R.id.	fragm_my_ll6);
		mLinearLayout7 = (LinearLayout) view.findViewById(R.id.	fragm_my_ll7);
		mHeadImageView = (ImageView) view.findViewById(R.id.head_img);
		mPhoneNumView = (TextView) view.findViewById(R.id.aPhoneNum);
		mZhangHu= (TextView) view.findViewById(R.id.changhuAdmin);
		mVersionDescTV = (TextView) view.findViewById(R.id.versionDesc);
		mLinearLayout1.setOnClickListener(this);
		mLinearLayout2.setOnClickListener(this);
		mLinearLayout3.setOnClickListener(this);
		mLinearLayout4.setOnClickListener(this);
		mLinearLayout5.setOnClickListener(this);
		//mLinearLayout6.setOnClickListener(this);
		mLinearLayout7.setOnClickListener(this);

		mVersionDescTV.setText("版本:"+ AppInfoUtils.getAppInfoName(getActivity()));
		return view;		
	}
	@Override
	public void onResume() {
		super.onResume();
		if (isUserLogin()) {
			refleshUI();
		}
	}
	
	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(mReceiver);
		getActivity().unregisterReceiver(mClearUserReceiver);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.fragm_my_ll1:
				if (isUserLogin()) {
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					DialogFragment newFragment = SwitchUserDialogFragment.newInstance(mUserInfo.getUid(), 0);
					newFragment.show(ft, "sw_u_dialog");
				} else {
					mActivity.switchToActivity(LoginActivity.class, null, 0, false, false);
					return;
				}
				break;
			case R.id.fragm_my_ll2:
				if (isUserLogin()) {
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					DialogFragment newFragment = CompNDialogFragment.newInstance(mUserInfo.getUid());
					newFragment.show(ft, "cn_dialog");
				} else {
					mActivity.switchToActivity(LoginActivity.class, null, 0, false, false);
					return;
				}
				break;
			case R.id.fragm_my_ll3://我的订单
				if (isUserLogin()) {
					Intent intent2=new Intent(getActivity(), MyOrderActivity.class);
					Bundle bundle2=new Bundle();
					bundle2.putInt("mUid",mUserInfo.getUid());
					intent2.putExtra("mBundle",bundle2);
					startActivity(intent2);
				} else {
					mActivity.switchToActivity(LoginActivity.class, null, 0, false, false);
					return;
				}
				break;
			case R.id.fragm_my_ll4:
				if (isUserLogin()) {
					Bundle bundle = new Bundle();
					bundle.putInt("uid", mUserInfo.getUid());
					mActivity.switchToActivity(ServerLookBoardActivity.class, bundle, 0, false, false);
				} else {
					mActivity.switchToActivity(LoginActivity.class, null, 0, false, false);
					return;
				}
				break;
			case R.id.fragm_my_ll5://账户管理
				if (isUserLogin()) {
					Intent intent = new Intent(getActivity(), ZhangHuMessage.class);
					Bundle bundle = new Bundle();
					bundle.putInt("mUid", mUserInfo.getUid());
					bundle.putLong("mPhone", mUserInfo.getPhoneNum());
					bundle.putString("mName", mUserInfo.getUserName());
					intent.putExtra("mBundle", bundle);
					startActivity(intent);
					//mActivity.switchToActivity(ZhangHuManager.class,bundle,0,false,false);
				}else {
					mActivity.switchToActivity(LoginActivity.class, null, 0, false, false);
					return;
				}
				break;

			case R.id.fragm_my_ll7:
				if (isUserLogin()) {
					Intent intent2 = new Intent(getActivity(), CompleteMessage.class);
					Bundle bundle2 = new Bundle();
					bundle2.putInt("mUid", mUserInfo.getUid());
					intent2.putExtra("mBundle", bundle2);
					startActivity(intent2);
				}else {
				mActivity.switchToActivity(LoginActivity.class, null, 0, false, false);
				return;
			}
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
		LogHelper.i(Tag, "mUserInfo.getUid() == " + mUserInfo.getUid() + "mUserInfo.getPhoneNum() == " + mUserInfo.getPhoneNum());
		return true;
		
	}
	
	private void refleshUI() {
		mPhoneNumView.setText("" + mUserInfo.getPhoneNum());
		//设置用户id为标签
		addTag(mUserInfo.getUid());// 添加用户标签
	}

	// 添加用户推送筛选标签
	private void addTag(int uid) {
		new AddTagTask(uid+"").execute();
		LogHelper.i(TAG, "------------------uid---" + uid);
		//App.getmPushAgent().getTagManager().add(uid + "");
	}
	class AddTagTask extends AsyncTask<Void, Void, String> {
		String tagString;
		String[] tags;

		public AddTagTask(String tag) {
			// TODO Auto-generated constructor stub
			tagString = tag;
			tags = tagString.split(",");
		}
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				TagManager.Result result = App.getmPushAgent().getTagManager().add(tags);
				LogHelper.d(TAG, result.toString());
				return result.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// edTag.setText("");
			// updateInfo("Add Tag:\n" + result);
		}
	}
	/**
	 * 清空用户后
	 */
	private void refleshUI2() {
		mPhoneNumView.setText("");
	}

}
