package com.xht.android.companyhelp;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
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
	private ArticleListFragment[] mArticleListFragments = new ArticleListFragment[3];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		for (int i = 0; i < 3; i++) {
			mArticleListFragments[i] = new ArticleListFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("witchF", i + 1);
			mArticleListFragments[i].setArguments(bundle);
		}
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
		radioButton1.setTextColor(Color.BLACK);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tab1:
					if (mCurFragment != 0) {
						viewPager.setCurrentItem(0);
						mCurFragment = 0;
					}
					radioButton1.setTextColor(Color.BLACK);
					radioButton2.setTextColor(Color.GRAY);
					radioButton3.setTextColor(Color.GRAY);
					break;
				case R.id.tab2:
					if (mCurFragment != 1) {
						viewPager.setCurrentItem(1);
						mCurFragment = 1;
					}
					radioButton1.setTextColor(Color.GRAY);
					radioButton2.setTextColor(Color.BLACK);
					radioButton3.setTextColor(Color.GRAY);
					break;
				case R.id.tab3:
					if (mCurFragment != 2) {
						viewPager.setCurrentItem(2);
						mCurFragment = 2;
					}
					radioButton1.setTextColor(Color.GRAY);
					radioButton2.setTextColor(Color.GRAY);
					radioButton3.setTextColor(Color.BLACK);
					break;

				default:
					break;
				}
			}
		});
		viewPager.setAdapter(new FragmentPagerAdapter(((FragmentActivity) getActivity()).getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return 3;
			}

			@Override
			public android.support.v4.app.Fragment getItem(int position) {
				return mArticleListFragments[position];
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
