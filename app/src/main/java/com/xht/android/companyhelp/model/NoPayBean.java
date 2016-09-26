package com.xht.android.companyhelp.model;

/**
 * Created by Administrator on 2016-9-23.
 */
public class NoPayBean  {


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
}
