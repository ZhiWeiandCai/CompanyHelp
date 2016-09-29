package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.provider.MyDatabaseManager;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONObject;

public class LoginFragment extends Fragment {
	
	private Button mLoginButton;
	TextView mButtonReg, mWangJi;
	private LoginActivity mActivity;
	private EditText mEditText1, mEditText2;
	private ProgressDialog mProgressDialog;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (LoginActivity) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity.setTitle(R.string.login_changhu);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		mWangJi = (TextView) view.findViewById(R.id.fotgetMMBt);
		mWangJi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogHelper.i("111", "aaa");
//				Intent intent = new Intent(getActivity(), ForgetActivity.class);
//				startActivity(intent);
				ForgetFragment fragment = new ForgetFragment();
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				
				ft.add(R.id.login_container, fragment, "ForgetFragment");
				ft.addToBackStack(null);
				ft.hide(LoginFragment.this);
				ft.commit();
			}
		});
		mButtonReg = (TextView) view.findViewById(R.id.zhuceBt);
		mButtonReg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mActivity.switchToActivity(RegisterActivity.class, null, 0, false, false);
				mActivity.finish();
			}
		});
		mLoginButton = (Button) view.findViewById(R.id.loginBt);
		mLoginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				postLogin();
			}
		});
		mEditText1 = (EditText) view.findViewById(R.id.phoneEdit);
		mEditText2 = (EditText) view.findViewById(R.id.mimaEdit);
		return view;		
	}
	
	private void postLogin() {
		String pNum = mEditText1.getText().toString();
		if (TextUtils.isEmpty(pNum)) {
			App.getInstance().showToast(getResources().getString(R.string.tip_input_pnum));
			return;
		}
		String mimaString = mEditText2.getText().toString();
		if (TextUtils.isEmpty(mimaString)) {
			App.getInstance().showToast(getResources().getString(R.string.tip_input_password));
			return;
		}
		createProgressDialogTitle(getResources().getString(R.string.zhengzai_login));
		VolleyHelpApi.getInstance().postLogin(pNum, mimaString, new APIListener() {
			
			@Override
			public void onResult(Object result) {
				dismissProgressDialog();
				App.getInstance().showToast(getResources().getString(R.string.success_login));
				long pNum = Long.parseLong(((JSONObject) result).optString("telephone", "0"));
				int userId = ((JSONObject) result).optInt("ordContactId", 0);
				ContentValues cv = new ContentValues();
				cv.put(MyDatabaseManager.MyDbColumns.UID, userId);
				cv.put(MyDatabaseManager.MyDbColumns.PHONE, pNum);
				getActivity().getContentResolver().insert(MyDatabaseManager.MyDbColumns.CONTENT_URI, cv);
				LogHelper.i("LoginFragment", "phoneNum" + pNum);
				Intent intent = new Intent(MyFragment.BRO_ACT_S);
				intent.putExtra(MyFragment.UID_KEY, userId);
				intent.putExtra(MyFragment.PHONENUM_KEY, pNum);
				getActivity().sendBroadcast(intent);
				getActivity().finish();
			}
			
			@Override
			public void onError(Object e) {
				dismissProgressDialog();
				App.getInstance().showToast(e.toString());
			}
		});
	}
	
	/**
	 * 创建mProgressDialog
	 */
	private void createProgressDialogTitle(String title)
	{
		if(mProgressDialog == null)
		{
			mProgressDialog = new ProgressDialog(getActivity());
		}
		mProgressDialog.setTitle(title);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}
	
	/**
	 * 隐藏mProgressDialog
	 */
	private void dismissProgressDialog()
	{
		if(mProgressDialog != null)
		{
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

}
