package com.yan.pathinfo

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/3/15
 * TextView 字符串快照
 */
class TxtSnapshot {
    companion object {
        private const val MAX_TXT = 15
        private const val MAX_TXT_LENGTH = 30
        private const val SPLIT = " | "
    }

    /**
     * 预先分配足够的空间
     */
    private val txtBuilder = StringBuilder(MAX_TXT * MAX_TXT_LENGTH + MAX_TXT * SPLIT.length)
    private val viewCache = ArrayList<View>(50)

    fun snapshot(activity: Activity): String? {
        txtBuilder.clear()
        val decorView = activity.window?.decorView ?: return null
        viewCache.add(decorView)

        while (viewCache.isNotEmpty()) {
            val v = viewCache.removeAt(viewCache.lastIndex)
            if (v is ViewGroup) {
                for (i in 0 until v.childCount) {
                    viewCache.add(v.getChildAt(i))
                }
            } else if (v is TextView) {
                val txt = v.text
                txtBuilder.append(SPLIT)
                    .append(txt.subSequence(0, MAX_TXT_LENGTH.coerceAtMost(txt.length)))
            }
        }
        return txtBuilder.toString()
    }
}