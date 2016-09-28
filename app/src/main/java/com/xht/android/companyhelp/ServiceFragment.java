package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xht.android.companyhelp.model.UserInfo;
import com.xht.android.companyhelp.provider.MyDatabaseManager;
import com.xht.android.companyhelp.util.LogHelper;

public class ServiceFragment extends Fragment implements OnClickListener{
	private static final String Tag = "ServiceFragment";

	private UserInfo mUserInfo;
	private MainActivity mActivity;
	private Button mCallPhone;
	private TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6,
		mTextView7, mTextView8, mTextView9, mTextView10, mTextView11, mTextView12;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserInfo = mActivity.mUserInfo;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_service, container, false);
		mCallPhone= (Button) view.findViewById(R.id.play_phonenum);
		mTextView1 = (TextView) view.findViewById(R.id.tv11);
		mTextView2 = (TextView) view.findViewById(R.id.tv12);
		mTextView3 = (TextView) view.findViewById(R.id.tv13);
		mTextView4 = (TextView) view.findViewById(R.id.tv21);
		mTextView5 = (TextView) view.findViewById(R.id.tv22);
		mTextView6 = (TextView) view.findViewById(R.id.tv23);
		mTextView7 = (TextView) view.findViewById(R.id.tv31);
		mTextView8 = (TextView) view.findViewById(R.id.tv32);
		mTextView9 = (TextView) view.findViewById(R.id.tv33);
		mTextView10 = (TextView) view.findViewById(R.id.tv41);
		mTextView11 = (TextView) view.findViewById(R.id.tv42);
		mTextView12 = (TextView) view.findViewById(R.id.tv43);

		mCallPhone.setOnClickListener(this);
		mTextView1.setOnClickListener(this);
		mTextView2.setOnClickListener(this);
		mTextView3.setOnClickListener(this);
		mTextView4.setOnClickListener(this);
		mTextView5.setOnClickListener(this);
		mTextView6.setOnClickListener(this);
		mTextView7.setOnClickListener(this);
		mTextView8.setOnClickListener(this);
		mTextView9.setOnClickListener(this);
		mTextView10.setOnClickListener(this);
		mTextView11.setOnClickListener(this);
		return view;		
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		if (!isUserLogin()) {
			intent = new Intent(getActivity(), LoginActivity.class);
			getActivity().startActivity(intent);
			return;
		}
		Bundle bundle = new Bundle();
		bundle.putInt("uid", mUserInfo.getUid());
		bundle.putLong("uphone", mUserInfo.getPhoneNum());
		bundle.putString("uname", mUserInfo.getUserName());
		switch (v.getId()) {
		case R.id.tv11:
			intent = new Intent(getActivity(), ZhuCeCompanyActivity.class);
			intent.putExtra("uData", bundle);
			getActivity().startActivity(intent);
			break;
		case R.id.tv12:
			intent = new Intent(getActivity(),DaiLiJIZhangActivity.class);
			intent.putExtra("uData", bundle);
			getActivity().startActivity(intent);
			break;
		case R.id.tv13:
			intent = new Intent(getActivity(),SheBaoSActivity.class);
			intent.putExtra("uData", bundle);
			getActivity().startActivity(intent);
			break;
		case R.id.tv21:
			intent = new Intent(getActivity(),FaPiaoActivity.class);
			intent.putExtra("uData", bundle);
			getActivity().startActivity(intent);
			break;
		case R.id.tv22:
			intent = new Intent(getActivity(),RegiTrademaskActivity.class);
			intent.putExtra("uData", bundle);
			getActivity().startActivity(intent);
			break;
		case R.id.tv23:
			App.getInstance().showToast("此功能暂未开放");
			break;
		case R.id.tv31:
				intent = new Intent(getActivity(),BianGengService.class);
				intent.putExtra("uData", bundle);
				getActivity().startActivity(intent);
				break;
		case R.id.tv32:
				intent = new Intent(getActivity(),ZhuCeZiJinActivity.class);
				intent.putExtra("uData", bundle);
				getActivity().startActivity(intent);
				break;
		case R.id.tv33:
				intent = new Intent(getActivity(),CancelComActivity.class);
				intent.putExtra("uData", bundle);
				getActivity().startActivity(intent);
				break;
		case R.id.tv41:
			App.getInstance().showToast("此功能暂未开放");
			break;
		case R.id.tv42:
			App.getInstance().showToast("此功能暂未开放");
			break;
			case R.id.play_phonenum:
				//拨打客服电话
				//用intent启动拨打电话
				Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"4008448488"));
				startActivity(intent2);
				break;
		default:
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

}
