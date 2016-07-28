package com.xht.android.companyhelp.model;

public class UserInfo {
	
	private int mUid;
	private long mPhoneNum;
	private String mUserName;
	
	public UserInfo() {
		
	}
	
	public void setUid(int uid) {
		mUid = uid;
	}
	
	public int getUid() {
		return mUid;
	}
	
	public void setPhoneNum(long phoneNum) {
		mPhoneNum = phoneNum;
	}
	
	public long getPhoneNum() {
		return mPhoneNum;
	}
	
	public void setUserName(String userName) {
		mUserName = userName;
	}
	
	public String getUserName() {
		return mUserName;
	}

}
