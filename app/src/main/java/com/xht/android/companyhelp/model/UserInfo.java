package com.xht.android.companyhelp.model;

public class UserInfo {
	
	private int mUid;
	private int mPhoneNum;
	private String mUserName;
	
	public UserInfo() {
		
	}
	
	public void setUid(int uid) {
		mUid = uid;
	}
	
	public int getUid() {
		return mUid;
	}
	
	public void setPhoneNum(int phoneNum) {
		mPhoneNum = phoneNum;
	}
	
	public int getPhoneNum() {
		return mPhoneNum;
	}
	
	public void setUserName(String userName) {
		mUserName = userName;
	}
	
	public String getUserName() {
		return mUserName;
	}

}
