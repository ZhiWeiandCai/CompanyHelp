package com.xht.android.companyhelp.model;

public class Article {
	
	private int mArtId;
	private String mTitle;
	private int mLeiBie;	//文章属于哪一类（1。政策，2.学堂，3.活动）

	private long mShijian;	//发表时间

	private String mSmallLeiBie;	//文章小类别

	private String mArtUrl;
	private String mArtPicUrl;
	
	public Article(int artId, String title, long shijian, String artUrl, String artPicUrl, String sLeiBie) {
		mArtId = artId;
		mTitle = title;
		mShijian = shijian;
		mArtUrl = artUrl;
		mArtPicUrl = artPicUrl;
		mSmallLeiBie = sLeiBie;
	}
	
	public void setId(int id) {
		mArtId = id;
	}
	
	public int getId() {
		return mArtId;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public void setPicUrl(String artPicUrl) {
		mArtPicUrl = artPicUrl;
	}
	
	public String getArtPicUrl() {
		return mArtPicUrl;
	}

	public long getmShijian() {
		return mShijian;
	}

	public void setmShijian(long mShijian) {
		this.mShijian = mShijian;
	}

	public String getmArtUrl() {
		return mArtUrl;
	}

	public void setmArtUrl(String mArtUrl) {
		this.mArtUrl = mArtUrl;
	}

	public String getmSmallLeiBie() {
		return mSmallLeiBie;
	}

	public void setmSmallLeiBie(String mSmallLeiBie) {
		this.mSmallLeiBie = mSmallLeiBie;
	}

}
