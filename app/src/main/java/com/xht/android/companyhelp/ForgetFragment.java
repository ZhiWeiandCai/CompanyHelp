package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONObject;

public class ForgetFragment extends Fragment implements View.OnClickListener{
	
	private LoginActivity mActivity;
	private Button mGetYanZ, mNextZhuce;
	private EditText mPNEditText, mYanZhengEditText, mPassWordEditText;
	private ProgressDialog mProgressDialog;
	//消息
	private final int MSG_COUNTDOWN = 0x2000;//获取验证码倒计时(重置密码)
	private int mTimeCounter;
	private String mSerial;
	private int mUId;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
				case MSG_COUNTDOWN:
					if(mTimeCounter <= 0)
					{
						mGetYanZ.setEnabled(true);
						mGetYanZ.setText(R.string.get_yanzhen);
					} else {
						mGetYanZ.setText("" + mTimeCounter);
					}
					break;

				default:
					break;
			}
		}
	};

	/**
	 * 给mHandler发送消息
	 */
	private void sendMessage(int what)
	{
		Message message = new Message();
		message.what = what;
		mHandler.sendMessage(message);
	}

	/**
	 * 重置密码点击获取验证码，服务器响应之后，发送倒计时消息
	 */
	private void sendCountdownMessage()
	{
		mTimeCounter = 60;
		mGetYanZ.setEnabled(false);
		mGetYanZ.setText("" + mTimeCounter);
		new Thread(){
			@Override
			public void run() {
				while(true)
				{

					try {
						Thread.sleep(1000);
						mTimeCounter --;
						sendMessage(MSG_COUNTDOWN);
					} catch (InterruptedException e) {

						e.printStackTrace();
						return;
					}
					if(mTimeCounter == 0)
					{
						return;
					}
				}
			}
		}.start();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (LoginActivity) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity.setTitle("重置密码");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fotget, container, false);
		mGetYanZ = (Button) view.findViewById(R.id.get_yanzhengma);
		mNextZhuce = (Button) view.findViewById(R.id.nextZhuce);
		mGetYanZ.setOnClickListener(this);
		mNextZhuce.setOnClickListener(this);
		mPNEditText = (EditText) view.findViewById(R.id.phoneEdit);
		mYanZhengEditText = (EditText) view.findViewById(R.id.zhuce_yan);
		mPassWordEditText = (EditText) view.findViewById(R.id.mimaEdit);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.nextZhuce:

				commitZhuce();
				break;
			case R.id.get_yanzhengma:
				getVerCode();
				break;

			default:
				break;
		}
	}

	private void commitZhuce() {
		String pNum = mPNEditText.getText().toString();
		if (TextUtils.isEmpty(pNum)) {
			App.getInstance().showToast(getResources().getString(R.string.tip_input_pnum));
			return;
		}
		String mimaString = mPassWordEditText.getText().toString();
		if (TextUtils.isEmpty(mimaString)) {
			App.getInstance().showToast(getResources().getString(R.string.tip_input_password));
			return;
		}
		String yanzheng = mYanZhengEditText.getText().toString();
		if (TextUtils.isEmpty(yanzheng)) {
			App.getInstance().showToast(getResources().getString(R.string.tip_input_yanzheng));
			return;
		}
		createProgressDialogTitle("重置密码...");
		VolleyHelpApi.getInstance().postResetPassword(pNum, mimaString, yanzheng, new APIListener() {

			@Override
			public void onResult(Object result) {
				dismissProgressDialog();
				/*int userId = Integer.parseInt(((JSONObject) result).optString("id", "0"));
				long pNum = Long.parseLong(((JSONObject) result).optString("tel", "0"));
				//注册成功后写入数据库
				ContentValues cv = new ContentValues();
				cv.put(MyDatabaseManager.MyDbColumns.UID, userId);
				cv.put(MyDatabaseManager.MyDbColumns.PHONE, pNum);
				getActivity().getContentResolver().insert(MyDatabaseManager.MyDbColumns.CONTENT_URI, cv);
				LogHelper.i("用户信息", "userId=" + userId + "pNum=" + pNum);*/


				//设置用户id为标签
				/*try {
					App.getmPushAgent().getTagManager().add(userId+"");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Intent intent = new Intent(MyFragment.BRO_ACT_S);
				intent.putExtra(MyFragment.UID_KEY, userId);
				intent.putExtra(MyFragment.PHONENUM_KEY, pNum);
				getActivity().sendBroadcast(intent);*/
				App.getInstance().showToast("重置密码成功");
				getActivity().finish();
			}

			@Override
			public void onError(Object e) {
				dismissProgressDialog();
				App.getInstance().showToast(e.toString());
			}
		});
	}

	private void getVerCode() {
		String pNum = mPNEditText.getText().toString();
		if (TextUtils.isEmpty(pNum)) {
			App.getInstance().showToast(getResources().getString(R.string.tip_input_pnum));
			return;
		}
		createProgressDialogTitle(getResources().getString(R.string.waitting_verification));
		VolleyHelpApi.getInstance().getVerCodeReset(pNum, new APIListener() {

			@Override
			public void onResult(Object result) {
				dismissProgressDialog();
				sendCountdownMessage();
				mSerial = ((JSONObject)result).optString("verify");
				LogHelper.i("验证码", mSerial);
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
