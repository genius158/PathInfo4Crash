package com.yan.analysispageinfo.crash;

import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.yan.analysispageinfo.BuildConfig;
import com.yan.pathinfo.PathMgr;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import xcrash.ICrashCallback;
import xcrash.TombstoneManager;
import xcrash.TombstoneParser;
import xcrash.XCrash;


public class CrashManager {

    static final String TAG = "CrashManager";

    private _CrashActivityManager actManager;

    private Application application;

    private static CrashManager crashManager;

    private static boolean inited = false;

    public static void init(Application application) {
        if (inited) return;
        inited = true;

        crashManager = new CrashManager(application);
        crashManager.initXCrash();
    }

    private CrashManager(Application application) {
        this.application = application;
        this.actManager = new _CrashActivityManager();
        application.registerActivityLifecycleCallbacks(actManager);
    }

    private void initXCrash() {
        // The callback when App process crashed.
        // callback for java crash, native crash and ANR
        ICrashCallback callback = new ICrashCallback() {
            @Override
            public void onCrash(String logPath, String emergency) {
                Log.d(TAG, "log path: " + (logPath != null ? logPath : "(null)") + ", emergency: " + (
                        emergency != null ? emergency : "(null)"));

                if (BuildConfig.DEBUG)
                    debug(logPath, emergency);
                else {
                    actManager.finish();
                }

                try {
                    Class monitor = Class.forName("xcrash.ActivityMonitor");
                    Method getIns = monitor.getDeclaredMethod("getInstance");
                    getIns.setAccessible(true);
                    Object ins = getIns.invoke(null);
                    Field activities = monitor.getDeclaredField("activities");
                    activities.setAccessible(true);
                    activities.set(ins,null);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };

        int version = 0;
        try {
            version = application.getPackageManager()
                    .getPackageInfo(application.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // Initialize xCrash.
        XCrash.init(application,
                new XCrash.InitParameters().setAppVersion(String.valueOf(version))
                        .setJavaLogCountMax(10)
                        .setJavaDumpAllThreadsWhiteList(
                                new String[]{"^main$", "^Binder:.*", ".*Finalizer.*"})
                        .setJavaDumpAllThreadsCountMax(10)
                        .setJavaCallback(callback)
                        .setAnrCallback(callback)
                        .setNativeLogCountMax(10)
                        .setNativeDumpAllThreadsWhiteList(new String[]{
                                "^xcrash\\.sample$", "^Signal Catcher$", "^Jit thread pool$", ".*(R|r)ender.*",
                                ".*Chrome.*"
                        })
                        .setNativeDumpAllThreadsCountMax(10)
                        .setNativeCallback(callback)
                        .setPlaceholderCountMax(3)
                        .setJavaRethrow(false)
                        .setNativeRethrow(false)
                        .setPlaceholderSizeKb(512)
                        .setLogFileMaintainDelayMs(1000));
    }

    private void sendThenDeleteCrashLog(String logPath, String emergency) {
        //CrashPacker.getInstance().onCrash(logPath);
        TombstoneManager.deleteTombstone(logPath);
    }

    private void debug(String logPath, String emergency) {
        // Parse and save the crash info to a JSON file for debugging.
        Log.d(TAG, "debug debug debug   " + logPath + "    " + emergency);
        FileWriter writer = null;
        try {
            File debug = new File(application.getFilesDir() + "/tombstonesss/" + System.currentTimeMillis() + "debug.json");
//            File debug = new File(application.getExternalFilesDir("tombstonesss") + "/" + System.currentTimeMillis() + "debug.json");
            if (!debug.getParentFile().exists()) debug.getParentFile().mkdirs();
            debug.createNewFile();
            writer = new FileWriter(debug, false);
            JSONObject info = new JSONObject(TombstoneParser.parse(logPath, emergency));
            // 增加路径
            info.put("pathInfo", PathMgr.getPathInfo());
            // 增加 txtView 快照
            info.put("txtSnapshot", PathMgr.getTxtSnapshot());
            writer.write(info.toString());
            writer.flush();
        } catch (Exception e) {
            Log.d(TAG, "debug failed", e);
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
