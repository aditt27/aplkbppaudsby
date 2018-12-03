package com.adibu.aplk;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(MyApplication.this);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i("Activity Stopped", activity.getLocalClassName());

    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i("Activity Started", activity.getLocalClassName());

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.i("Activity SaveInstState", activity.getLocalClassName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i("Activity Resumed", activity.getLocalClassName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.i("Activity Paused", activity.getLocalClassName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.i("Activity Destroyed", activity.getLocalClassName());
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i("Activity Created", activity.getLocalClassName());
    }
}
