package com.example.admin.blockthis1;

/**
 * Created by admin on 16/03/18.
 */

public class RejectedDataModel {
    public int id;
    public String mname;
    public String mphoneNumber;
    public int mRejected;

    RejectedDataModel(int id,String name,String phoneNumber,int rejected)
    {
        this.id = id;
        this.mname = name;
        this.mphoneNumber = phoneNumber;
        this.mRejected = rejected;
    }

    public String getName(){
        return mname;
    }

    public void setName(String name) {
        this.mname = name;
    }

    public String getPhoneNumber() {
        return mphoneNumber;
    }

    public void setphoneNumber(String phoneNumber) {
        this.mphoneNumber = phoneNumber;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getRejected(){ return mRejected;}

    public void setRejected(int rejected){mRejected = rejected; }
}
