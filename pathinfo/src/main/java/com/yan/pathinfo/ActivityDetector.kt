package com.yan.pathinfo

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/3/15
 */
internal class ActivityDetector : Application.ActivityLifecycleCallbacks {
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {
        tryActivityPop(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityStack.add(activity)

        PathMgr.tryLoadOrRestorePath(savedInstanceState)
        PathMgr.pathIn(PathInfo.activity(activity, true))
        (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
            FragmentDetector(),
            true
        )
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        PathMgr.saveInstance(outState)
    }

    override fun onActivityDestroyed(activity: Activity) {
        tryActivityPop(activity)
        PathMgr.pathOut(PathInfo.activity(activity, false))
    }

    private val activityStack = LinkedHashSet<Activity>()
    private fun tryActivityPop(activity: Activity) {
        if (!isActivityFinish(activity)) return

        activityStack.remove(activity)
    }

    private fun isActivityFinish(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed) return true
        }
        return activity.isFinishing
    }

    fun getCurActivity(): Activity? {
        return activityStack.lastOrNull()
    }


}