package com.adibu.aplk;

import android.app.Application;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        InternetAvailabilityChecker.init(this);
    }
}
