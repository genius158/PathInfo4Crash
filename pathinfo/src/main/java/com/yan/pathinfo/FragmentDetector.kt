package com.yan.pathinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/3/15
 */
internal class FragmentDetector : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        PathMgr.pathIn(PathInfo.fragment(f, true))
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        PathMgr.pathOut(PathInfo.fragment(f, false))
    }
}