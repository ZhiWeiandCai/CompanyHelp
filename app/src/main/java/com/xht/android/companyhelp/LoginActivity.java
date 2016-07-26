package com.xht.android.companyhelp;

import android.R.color;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar bar = getActionBar();
                
        int actionBar_titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView view = (TextView) findViewById(actionBar_titleId);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(0xff000000);
        
        setTitle(null);
        
        setContentView(R.layout.activity_login);
                
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE;
        bar.setDisplayOptions(change);
        
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
