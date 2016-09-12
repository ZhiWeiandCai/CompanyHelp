package com.xht.android.companyhelp;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.provider.MyDatabaseManager;
import com.xht.android.companyhelp.util.LogHelper;

public class RegisterActivity extends Activity implements OnClickListener {

	private Button mGetYanZ, mNextZhuce;
	private EditText mPNEditText, mYanZhengEditText, mPassWordEditText;
	private ProgressDialog mProgressDialog;
	//消息
	private final int MSG_COUNTDOWN = 0x1000;//获取验证码倒计时(注册)
	private int mTimeCounter;
	private String mSerial;
	
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
	 * 注册界面点击获取验证码，服务器响应之后，发送倒计时消息
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int actionBar_titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView view = (TextView) findViewById(actionBar_titleId);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(0xff000000);
        setTitle(R.string.user_zhuce);
        setContentView(R.layout.activity_register);
        
        final ActionBar bar = getActionBar();
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE;
        bar.setDisplayOptions(change);
        
        mGetYanZ = (Button) findViewById(R.id.get_yanzhengma);
        mNextZhuce = (Button) findViewById(R.id.nextZhuce);
        mGetYanZ.setOnClickListener(this);
        mNextZhuce.setOnClickListener(this);
        mPNEditText = (EditText) findViewById(R.id.zhuce_phonenum);
        mYanZhengEditText = (EditText) findViewById(R.id.zhuce_yan);
        mPassWordEditText = (EditText) findViewById(R.id.mimaEdit);
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
		createProgressDialogTitle(getResources().getString(R.string.zhengzai_zhuce));
		VolleyHelpApi.getInstance().postZhuCe(pNum, mimaString, yanzheng, new APIListener() {
			
			@Override
			public void onResult(Object result) {
				dismissProgressDialog();
				int userId = Integer.parseInt(((JSONObject) result).optString("id", "0"));
				long pNum = Long.parseLong(((JSONObject) result).optString("tel", "0"));
				//注册成功后写入数据库
				ContentValues cv = new ContentValues();
				cv.put(MyDatabaseManager.MyDbColumns.UID, userId);
				cv.put(MyDatabaseManager.MyDbColumns.PHONE, pNum);
				RegisterActivity.this.getContentResolver().insert(MyDatabaseManager.MyDbColumns.CONTENT_URI, cv);
				LogHelper.i("用户信息", "userId=" + userId + "pNum=" + pNum);


				//设置用户id为标签
				try {
					App.getmPushAgent().getTagManager().add(userId+"");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Intent intent = new Intent(MyFragment.BRO_ACT_S);
				intent.putExtra(MyFragment.UID_KEY, userId);
				intent.putExtra(MyFragment.PHONENUM_KEY, pNum);
				sendBroadcast(intent);
				finish();
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
		VolleyHelpApi.getInstance().getVerCode(pNum, new APIListener() {
			
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
			mProgressDialog = new ProgressDialog(this);
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
