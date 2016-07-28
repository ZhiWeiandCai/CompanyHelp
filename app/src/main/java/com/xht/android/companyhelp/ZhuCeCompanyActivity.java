package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONObject;

/**
 * 服务里面的注册公司界面
 */
public class ZhuCeCompanyActivity extends Activity implements OnCheckedChangeListener {

	private LinearLayout mLLJiZEx;    //代理记账展开按钮-显示记账周期
	private LinearLayout mLLNaSR;        //代理记账展开按钮-显示纳税人类别
	private EditText mET;    //公司名称
	private TextView mHeJiTV;

	private int mMoney = 0;    //合计
	private int mAddTGMoney = 1188;    //地址托管
	private int mMoneyYouHui = 0;    //若选择代理记账服务，可优惠多少钱
	private String mArea = "";    //spinner注册区域所选的字符串
	private boolean mShiFouJiaJi;	//是否加急
	private boolean mAddTuoGuan;	//地址托管

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhucecompany);
		TextView mCustomView = new TextView(this);
		mCustomView.setGravity(Gravity.CENTER);
		mCustomView.setText("下单预约-公司注册");
		final ActionBar aBar = getActionBar();
		aBar.setCustomView(mCustomView,
				new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
		aBar.setDisplayOptions(change);
		final Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);    //注册区域的spinner
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.areas, android.R.layout.simple_spinner_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(arrayAdapter);
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				LogHelper.i("spinner1", spinner1.getSelectedItem().toString());
				mArea = spinner1.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		Switch yorNJiaJi = (Switch) findViewById(R.id.tSFJJBtn);	//是否加急
		yorNJiaJi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mShiFouJiaJi = isChecked;
				if (isChecked) {

				} else {

				}
				reFleshMoneyHeji();
			}
		});
		//地址托管
		Switch switch1 = (Switch) findViewById(R.id.toggleButton1);
		switch1.setOnCheckedChangeListener(this);
		//代理记账
		Switch switch2 = (Switch) findViewById(R.id.toggleButton2);
		switch2.setOnCheckedChangeListener(this);
		mLLJiZEx = (LinearLayout) findViewById(R.id.jiZLL);
		mLLNaSR = (LinearLayout) findViewById(R.id.leiNSRLL);
		//请选择起始步骤
		Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter.createFromResource(this, R.array.qishi_buzou, android.R.layout.simple_spinner_item);
		arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(arrayAdapter2);
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		((TextView) findViewById(R.id.tip_word_youhui_book)).setText(String.format(getResources().getString(R.string.bookjichang_youhui_s), mMoneyYouHui));
		((TextView) findViewById(R.id.shu_add)).setText(String.format(getResources().getString(R.string.address_tuoguan_ss), mAddTGMoney));
		mHeJiTV = ((TextView) findViewById(R.id.shu_heji));
		//下单预约
		Button yuYueBook = (Button) findViewById(R.id.bookYuQue);
		yuYueBook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		reFleshMoneyHeji();
	}

	/**
	 * 更新合计的View
	 */
	private void reFleshMoneyHeji() {
		mHeJiTV.setText(String.format(getResources().getString(R.string.heji_yuan), mMoney));
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
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
			case R.id.toggleButton2:
				if (isChecked) {
					mLLJiZEx.setVisibility(View.VISIBLE);
					mLLNaSR.setVisibility(View.VISIBLE);
				} else {
					mLLJiZEx.setVisibility(View.GONE);
					mLLNaSR.setVisibility(View.GONE);
				}
				break;
			case R.id.toggleButton1:
				mAddTuoGuan = isChecked;
				if (isChecked) {

				} else {

				}
				reFleshMoneyHeji();
				break;
			default:

				break;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
}
