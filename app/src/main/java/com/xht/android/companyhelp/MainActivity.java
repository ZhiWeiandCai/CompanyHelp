package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.tag.TagManager;
import com.xht.android.companyhelp.model.UserInfo;
import com.xht.android.companyhelp.util.LogHelper;


public class MainActivity extends FragmentActivity {
	
//	Fragment mSwitchFragment;
	public static UserInfo mUserInfo = new UserInfo();
	private static final String TAG = "MainActivity";
	

	public static UserInfo getInstance() {
		return mUserInfo;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setContentView(R.layout.activity_main);
		ImageView mCustomView = new ImageView(this);
		mCustomView.setBackgroundResource(R.mipmap.logo);
		final ActionBar aBar = getActionBar();
		aBar.setCustomView(mCustomView,
				new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams lp = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		lp.gravity = lp.gravity & ~Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		aBar.setCustomView(mCustomView, lp);
		int change = ActionBar.DISPLAY_SHOW_CUSTOM;
		aBar.setDisplayOptions(change);
		//友盟推送初始化
		PushAgent mPushAgent = PushAgent.getInstance(this);
		//mPushAgent.enable();
		PushAgent.getInstance(this).onAppStart();
		final String device_token = UmengRegistrar.getRegistrationId(this);
		LogHelper.i(TAG,"--------------"+device_token);

		
	

	}
	
	

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
    
    public void switchToActivity(Class cls, Bundle bundle,int flag,
			boolean withTransition, boolean isExit) {
		try {
			Intent intent = new Intent(this, cls);
			if(bundle != null){
				intent.putExtras(bundle);
			}
			if(flag != 0){
				intent.setFlags(flag);
			}
			startActivity(intent);
			if(withTransition){
				if (!isExit) {
//					overridePendingTransition(R.anim.push_in_right, R.anim.push_out_right);
				}else {
//					overridePendingTransition(R.anim.back_in_left, R.anim.back_out_left);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
