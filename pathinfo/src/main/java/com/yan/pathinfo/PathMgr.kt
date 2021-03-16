package com.yan.pathinfo

import android.app.Application
import android.os.Bundle
import java.io.File

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/3/15
 */
object PathMgr {
    private const val FILE_NAME = ".PathRecorder.pi"
    private const val BUNDLE_PATH_FROM_DISK = "loadFromDiskCache"

    // 地址记录
    private var pathRecorder: PathRecorder? = null

    // 存储恢复管理
    private var recorderCache: RecorderCache? = null

    private val activityDetector = ActivityDetector()
    private val txtSnapshot = TxtSnapshot()

    @JvmStatic
    fun initDetector(app: Application) {
        recorderCache = RecorderCache(app.cacheDir.absolutePath + File.separator + FILE_NAME)
        app.registerActivityLifecycleCallbacks(activityDetector)
    }

    @JvmStatic
    fun getPathInfo(): String? {
        return pathRecorder?.toString()
    }

    @JvmStatic
    fun getTxtSnapshot(): String? {
        return activityDetector.getCurActivity()?.let { txtSnapshot.snapshot(it) }
    }

    internal fun pathIn(path: String) {
        pathRecorder?.pathIn(path)
        recorderCache?.cache2Disk(pathRecorder)
    }

    internal fun pathOut(path: String) {
        pathRecorder?.pathOut(path)
        recorderCache?.cache2Disk(pathRecorder)
    }

    internal fun tryLoadOrRestorePath(savedInstanceState: Bundle?) {
        if (pathRecorder != null) return

        if (savedInstanceState != null) {
            pathRecorder = restore(savedInstanceState)
        }

        if (pathRecorder == null) {
            pathRecorder = PathRecorder()
        }
    }

    private fun restore(savedInstanceState: Bundle): PathRecorder? {
        val needLoadFromDisk = savedInstanceState.getBoolean(BUNDLE_PATH_FROM_DISK)
        if (!needLoadFromDisk) return null

        return recorderCache?.getRecorder()
    }


    internal fun saveInstance(outState: Bundle) {
        outState.putBoolean(BUNDLE_PATH_FROM_DISK, true)
    }
}