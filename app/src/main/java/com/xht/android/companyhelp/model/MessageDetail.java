package com.xht.android.companyhelp.model;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016-9-5.
 */
public class MessageDetail {
    private String mTitle;//推送通知的标题
    private String mContent;//推送通知的内容
    private String mTime;//记录通知到达的时间
    private String mUrl;//推送消息的url
    private String mMessUid;//推送消息的id
    private String mUrlIcom;//推送过来的图标url
    private Bitmap mBitmap;

    public String getmMessUid() {
        return mMessUid;
    }

    public void setmMessUid(String mMessUid) {
        this.mMessUid = mMessUid;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmUrlIcom() {
        return mUrlIcom;
    }

    public void setmUrlIcom(String mUrlIcom) {
        this.mUrlIcom = mUrlIcom;
    }
}
