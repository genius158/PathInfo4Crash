package com.yan.analysispageinfo

import android.app.Application
import com.yan.analysispageinfo.crash.CrashManager
import com.yan.pathinfo.PathMgr

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/3/15
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        PathMgr.initDetector(this)
        CrashManager.init(this)
    }

}