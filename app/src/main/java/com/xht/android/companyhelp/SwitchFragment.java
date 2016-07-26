package com.xht.android.companyhelp;

import com.xht.android.companyhelp.util.LogHelper;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SwitchFragment extends Fragment {
	private static final String TAG = "SwitchFragment";
	private RadioGroup mRadioGroup;
	private RadioButton mRadioButton1, mRadioButton2, mRadioButton3, mRadioButton4;
	private Fragment mFragment1, mFragment2, mFragment3, mFragment4;
	private final int[] mSwitchIds = {R.id.radio_first, R.id.radio_service, R.id.radio_mess, R.id.radio_my};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			mFragment1 = fm.findFragmentByTag("f1");
	        if (mFragment1 == null) {
	            mFragment1 = new MainFragment();
	            ft.add(R.id.contain1, mFragment1, "f1");
	        }
	        mFragment2 = fm.findFragmentByTag("f2");
	        if (mFragment2 == null) {
	            mFragment2 = new ServiceFragment();
	            ft.add(R.id.contain1, mFragment2, "f2");
	        }
	        mFragment3 = fm.findFragmentByTag("f3");
	        if (mFragment3 == null) {
	            mFragment3 = new MessFragment();
	            ft.add(R.id.contain1, mFragment3, "f3");
	        }
	        mFragment4 = fm.findFragmentByTag("f4");
	        if (mFragment4 == null) {
	            mFragment4 = new MyFragment();
	            ft.add(R.id.contain1, mFragment4, "f4");
	        }
	        ft.commit();
	        
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_switch, container, false);
		mRadioGroup = (RadioGroup) view.findViewById(R.id.menu_Switch);
		mRadioButton1 = (RadioButton) view.findViewById(R.id.radio_first);
		mRadioButton2 = (RadioButton) view.findViewById(R.id.radio_service);
		mRadioButton3 = (RadioButton) view.findViewById(R.id.radio_mess);
		mRadioButton4 = (RadioButton) view.findViewById(R.id.radio_my);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				updateFragmentVisibility();
				switch (checkedId) {
				case R.id.radio_first:
					
					break;
				case R.id.radio_service:
					
					break;
				case R.id.radio_mess:
	
					break;
				case R.id.radio_my:
	
					break;

				default:
					break;
				}
			}
		});
		updateFragmentVisibility();
		return view;		
	}
	
	// Update fragment visibility based on current check box state.
    void updateFragmentVisibility() {
    	
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (mRadioButton1.isChecked()) {
        	ft.show(mFragment1);
        	
        } else {
        	ft.hide(mFragment1);
        }
        
        if (mRadioButton2.isChecked()) ft.show(mFragment2);
        else ft.hide(mFragment2);
        if (mRadioButton3.isChecked()) ft.show(mFragment3);
        else ft.hide(mFragment3);
        if (mRadioButton4.isChecked()) ft.show(mFragment4);
        else ft.hide(mFragment4);
        ft.commit();
        
    }

}
