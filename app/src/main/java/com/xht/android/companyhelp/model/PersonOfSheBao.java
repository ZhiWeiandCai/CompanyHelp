package com.xht.android.companyhelp.model;

/**
 * Created by Administrator on 2016/8/17.
 * 社保业务里的人对象
 */
public class PersonOfSheBao {

    private String mIdCard; //身份证号
    private String mName;
    private int isCheck = 1;    //默认为购买

    public PersonOfSheBao() {

    }

    public int isCheck() {
        return isCheck;
    }

    public void setCheck(int check) {
        isCheck = check;
    }

    public String getmIdCard() {
        return mIdCard;
    }

    public String getmName() {
        return mName;
    }

    public void setmIdCard(String mIdCard) {
        this.mIdCard = mIdCard;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
