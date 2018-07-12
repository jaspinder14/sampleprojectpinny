package com.mogo.controller;

import android.app.Application;
import android.os.StrictMode;

import com.mogo.controller.Fcm.MyFirebaseInstanceIDService;

/**
 * Created by Mobile on 9/12/2017.
 */

public class App extends Application {

    public static App context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        new MyFirebaseInstanceIDService();
        new SharedPref(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public static App getinstance() {
        return context;
    }

}
