package com.xht.android.companyhelp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ServiceFragment extends Fragment implements OnClickListener{
	
	private TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6,
		mTextView7, mTextView8, mTextView9, mTextView10, mTextView11, mTextView12;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_service, container, false);
		mTextView1 = (TextView) view.findViewById(R.id.tv11);
		mTextView2 = (TextView) view.findViewById(R.id.tv12);
		mTextView3 = (TextView) view.findViewById(R.id.tv13);
		mTextView4 = (TextView) view.findViewById(R.id.tv21);
		mTextView5 = (TextView) view.findViewById(R.id.tv22);
		mTextView6 = (TextView) view.findViewById(R.id.tv23);
		mTextView7 = (TextView) view.findViewById(R.id.tv31);
		mTextView8 = (TextView) view.findViewById(R.id.tv32);
		mTextView9 = (TextView) view.findViewById(R.id.tv33);
		mTextView10 = (TextView) view.findViewById(R.id.tv41);
		mTextView11 = (TextView) view.findViewById(R.id.tv42);
		mTextView12 = (TextView) view.findViewById(R.id.tv43);
		mTextView1.setOnClickListener(this);
		return view;		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv11:
			Intent intent = new Intent(getActivity(), ZhuCeCompanyActivity.class);
			getActivity().startActivity(intent);
			break;

		default:
			break;
		}
	}

}
