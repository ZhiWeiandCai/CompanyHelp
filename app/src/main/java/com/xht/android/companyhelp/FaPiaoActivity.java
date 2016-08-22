package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FaPiaoActivity extends Activity {

    RadioGroup rg;
    RadioButton mRBtn1, mRBtn2, mRBtn3;
    TextView mJinETV;
    Button mBookXiaDan;
    private Fragment mFragment1, mFragment2, mFragment3;
    private int mCurFragFlag;   //标记当前是哪一个Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_piao);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mFragment1 = fm.findFragmentByTag("f1");
        if (mFragment1 == null) {
            mFragment1 = new FaPiao1Fragment();
        }
        ft.add(R.id.fragment_contain, mFragment1, "f1");
        mFragment2 = fm.findFragmentByTag("f2");
        if (mFragment2 == null) {
            mFragment2 = new FaPiao2Fragment();
        }
        ft.add(R.id.fragment_contain, mFragment2, "f2");
        mFragment3 = fm.findFragmentByTag("f3");
        if (mFragment3 == null) {
            mFragment3 = new FaPiao3Fragment();
        }
        ft.add(R.id.fragment_contain, mFragment3, "f3");
        ft.commit();
        initView();
        updateFragmentVisibility();
    }

    private void initView() {
        rg = (RadioGroup) findViewById(R.id.switch_tabs);
        mRBtn1 = (RadioButton) findViewById(R.id.tab1);
        mRBtn2 = (RadioButton) findViewById(R.id.tab2);
        mRBtn3 = (RadioButton) findViewById(R.id.tab3);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateFragmentVisibility();
                switch (checkedId) {
                    case R.id.tab1:

                        break;
                    case R.id.tab2:
                        break;
                    case R.id.tab3:
                        break;

                    default:
                        break;
                }
            }
        });
        mJinETV = (TextView) findViewById(R.id.shu_heji);
        mBookXiaDan = (Button) findViewById(R.id.bookYuQue);
    }

    // Update fragment visibility based on current check box state.
    void updateFragmentVisibility() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (mRBtn1.isChecked()) {
            ft.show(mFragment2);
        } else {
            ft.hide(mFragment2);
        }
        if (mRBtn2.isChecked()) ft.show(mFragment1);
        else ft.hide(mFragment1);
        if (mRBtn3.isChecked()) ft.show(mFragment3);
        else ft.hide(mFragment3);
        ft.commit();
    }
}
