package com.example.blooddonation;
import android.app.Application;

public class MyApplication  extends  Application{

    String usrMail =  "";

    public String getData()
    {
        return this.usrMail;
    }
    public void setData(String val)
    {
        this.usrMail = val;
    }
}
