package com.example.blooddonation.Service;

import android.content.Context;
import android.content.SharedPreferences;

public class saveData {


    ///saving data
    public static  void save(Context ctx , String usrMail,String value)
    {
        SharedPreferences s = ctx.getSharedPreferences("Email",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = s.edit();
        edit.putString(usrMail,value);
        edit.apply();

    }

    public static String read(Context ctx,String name,String defaultVal)
    {
        SharedPreferences s = ctx.getSharedPreferences("Email",Context.MODE_PRIVATE);
        return  s.getString(name,defaultVal);

    }
}
