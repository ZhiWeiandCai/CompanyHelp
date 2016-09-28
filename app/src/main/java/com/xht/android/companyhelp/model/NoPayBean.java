package com.xht.android.companyhelp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016-9-23.
 */
public class NoPayBean implements Parcelable {


    /**
     * businezzType : 10
     * hasAccount : N
     * placeOrderTime : 2016-09-23 17:01:05
     * orderid : 5
     * orderName : 注册公司
     * orderFee : 3
     */

    private String businezzType;
    private String hasAccount;
    private String placeOrderTime;
    private String orderid;
    private String orderName;
    private String orderFee;

    public String getBusinezzType() {
        return businezzType;
    }

    public void setBusinezzType(String businezzType) {
        this.businezzType = businezzType;
    }

    public String getHasAccount() {
        return hasAccount;
    }

    public void setHasAccount(String hasAccount) {
        this.hasAccount = hasAccount;
    }

    public String getPlaceOrderTime() {
        return placeOrderTime;
    }

    public void setPlaceOrderTime(String placeOrderTime) {
        this.placeOrderTime = placeOrderTime;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderFee() {
        return orderFee;
    }

    public void setOrderFee(String orderFee) {
        this.orderFee = orderFee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.businezzType);
        dest.writeString(this.hasAccount);
        dest.writeString(this.placeOrderTime);
        dest.writeString(this.orderid);
        dest.writeString(this.orderName);
        dest.writeString(this.orderFee);
    }

    public NoPayBean() {
    }

    protected NoPayBean(Parcel in) {
        this.businezzType = in.readString();
        this.hasAccount = in.readString();
        this.placeOrderTime = in.readString();
        this.orderid = in.readString();
        this.orderName = in.readString();
        this.orderFee = in.readString();
    }

    public static final Parcelable.Creator<NoPayBean> CREATOR = new Parcelable.Creator<NoPayBean>() {
        @Override
        public NoPayBean createFromParcel(Parcel source) {
            return new NoPayBean(source);
        }

        @Override
        public NoPayBean[] newArray(int size) {
            return new NoPayBean[size];
        }
    };
}
