package com.mogo.controller;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPref {

    private final SharedPreferences pref;
    private static SharedPref mInstance;

    public SharedPref(Context context) {
         mInstance = this;
         pref = context.getSharedPreferences("Mogo",0);

    }

    public static SharedPref getInstance(Context context) {
        return new SharedPref(context);
    }

    public void setInt(String name, int value) {
        pref.edit().putInt(name, value).commit();
    }

    public Integer getInt(String name) {
        return pref.getInt(name, 0);
    }

    public String setString(String name, String value){
        pref.edit().putString(name, value).commit();
        return name;
    }

    public String getString(String name) {
        return pref.getString(name, "");
    }

    public void setboolen(String name,boolean value){
        pref.edit().putBoolean(name,value).commit();
    }

    public boolean getboolen(String name) {
        return pref.getBoolean(name, false);
    }

    public void clear() {
        pref.edit().clear().commit();
    }
}
