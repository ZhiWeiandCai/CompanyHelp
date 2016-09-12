package com.xht.android.companyhelp;

import com.umeng.message.ALIAS_TYPE;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.xht.android.companyhelp.model.UserInfo;
import com.xht.android.companyhelp.util.LogHelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends FragmentActivity {
	
//	Fragment mSwitchFragment;
	public UserInfo mUserInfo = new UserInfo();
	private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setContentView(R.layout.activity_main);
        
        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        mSwitchFragment = fragmentManager.findFragmentById(R.id.switchFragment);*/
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
