package com.xht.android.companyhelp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainFragment extends Fragment {
	
	private RadioButton radioButton1, radioButton2, radioButton3;
	RadioGroup rg;
	private ViewPager viewPager;
	private int mCurFragment;
	private int[] tabIds = {R.id.tab1, R.id.tab2, R.id.tab3};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		rg = (RadioGroup) view.findViewById(R.id.switch_tabs);
		
		radioButton1 = (RadioButton) view.findViewById(R.id.tab1);
		radioButton2 = (RadioButton) view.findViewById(R.id.tab2);
		radioButton3 = (RadioButton) view.findViewById(R.id.tab3);
		
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tab1:
					if (mCurFragment != 0) {
						viewPager.setCurrentItem(0);
						mCurFragment = 0;
					}
					break;
				case R.id.tab2:
					if (mCurFragment != 1) {
						viewPager.setCurrentItem(1);
						mCurFragment = 1;
					}
					break;
				case R.id.tab3:
					if (mCurFragment != 2) {
						viewPager.setCurrentItem(2);
						mCurFragment = 2;
					}
					break;

				default:
					break;
				}
			}
		});
		viewPager.setAdapter(new FragmentStatePagerAdapter(((FragmentActivity) getActivity()).getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				
				return 3;
			}
			
			@Override
			public android.support.v4.app.Fragment getItem(int arg0) {
				
				return new ArticleListFragment();
			}
		});
		viewPager.setCurrentItem(mCurFragment);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if (mCurFragment != arg0) {
					rg.check(tabIds[arg0]);
					mCurFragment = arg0;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		return view;		
	}

}
