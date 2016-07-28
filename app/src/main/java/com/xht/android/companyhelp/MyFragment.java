package com.xht.android.companyhelp;

import com.xht.android.companyhelp.model.UserInfo;
import com.xht.android.companyhelp.provider.MyDatabaseManager;
import com.xht.android.companyhelp.util.LogHelper;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyFragment extends Fragment implements OnClickListener {
	private static final String Tag = "MyFragment";
	public static final String BRO_ACT_S = "com.xht.android.companyhelp.bro_act_s";
	public static final String PHONENUM_KEY = "phone_key";
	public static final String UID_KEY = "userId_key";
	private LinearLayout mLinearLayout1, mLinearLayout2;
	private ImageView mHeadImageView;
	private MainActivity mActivity;
	private UserInfo mUserInfo;
	private TextView mPhoneNumView;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			int uId = intent.getIntExtra(UID_KEY, 0);
			long phoneNum = intent.getLongExtra(PHONENUM_KEY, 0);
			LogHelper.i(Tag, "" + phoneNum);
			mUserInfo.setUid(uId);
			mUserInfo.setPhoneNum(phoneNum);
			refleshUI();
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
		mUserInfo = ((MainActivity) mActivity).mUserInfo;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_my, container, false);
		mLinearLayout1 = (LinearLayout) view.findViewById(R.id.fragm_my_ll1);
		mLinearLayout2 = (LinearLayout) view.findViewById(R.id.fragm_my_ll2);
		mHeadImageView = (ImageView) view.findViewById(R.id.head_img);
		mPhoneNumView = (TextView) view.findViewById(R.id.aPhoneNum);
		mLinearLayout1.setOnClickListener(this);
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
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragm_my_ll1:
			if (isUserLogin()) {
				
			} else {
				mActivity.switchToActivity(LoginActivity.class, null, 0, false, false);
				return;
			}			
		
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
	}

}
