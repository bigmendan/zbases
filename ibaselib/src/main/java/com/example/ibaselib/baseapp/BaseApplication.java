package com.example.ibaselib.baseapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by 大麻子
 */

public class BaseApplication extends Application {


    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
