package com.xht.android.companyhelp.model;

/**
 * Created by Administrator on 2016-9-5.
 */
public class PayDetail {
    private String mUid;//用户id
    private String mTitle;//标题
    private String mTime;//时间
    private String commen;//备注
    private String mOrderNumber;//订单编号
    private String mTotalMoney;//下单金额
    private int method;//下单支付方式

    private String busyer;//商家账号

    public String getBusyer() {
        return busyer;
    }

    public void setBusyer(String busyer) {
        this.busyer = busyer;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getCommen() {
        return commen;
    }

    public void setCommen(String commen) {
        this.commen = commen;
    }

    public String getmOrderNumber() {
        return mOrderNumber;
    }

    public void setmOrderNumber(String mOrderNumber) {
        this.mOrderNumber = mOrderNumber;
    }

    public String getmTotalMoney() {
        return mTotalMoney;
    }

    public void setmTotalMoney(String mTotalMoney) {
        this.mTotalMoney = mTotalMoney;
    }
}
