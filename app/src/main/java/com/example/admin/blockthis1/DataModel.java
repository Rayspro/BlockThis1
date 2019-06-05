package com.example.admin.blockthis1;

/**
 * Created by admin on 13/02/18.
 */

public class DataModel {
    public String name;
    public String phoneNumber;
    public int id;

    DataModel(int id, String name,String phoneNumber)
    {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String slotno) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setphoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

}


