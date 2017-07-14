package com.sollian.demo;

import android.app.Application;

/**
 * @author sollian on 2017/7/14.
 */

public class MyApplication extends Application {
    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
