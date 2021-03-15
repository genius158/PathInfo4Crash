package com.yan.analysispageinfo.crash;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Process;

import java.lang.ref.WeakReference;

class _CrashActivityManager implements Application.ActivityLifecycleCallbacks {

    private WeakReference<Activity> curActivity = new WeakReference<Activity>(null);

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        curActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    public void finish() {
        Activity cur = curActivity.get();
        if (cur != null) cur.finishAffinity();
        Process.killProcess(Process.myPid());
    }
}
