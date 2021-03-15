package com.yan.pathinfo

import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import java.io.*

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/3/15
 */
object PathMgr {
    private const val BUNDLE_PATH_FROM_DISK = "loadFromDiskCache"

    // 地址记录
    private var pathRecorder: PathRecorder? = null

    // 缓存路径 文件地址
    private var cacheFilePath: String? = null


    private val activityDetector = ActivityDetector()
    private val txtSnapshot = TxtSnapshot()

    @JvmStatic
    fun initDetector(app: Application) {
        cacheFilePath = app.cacheDir.absolutePath + File.separator + ".PathRecorder.pi"
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
        save2Disk()
    }

    internal fun pathOut(path: String) {
        pathRecorder?.pathOut(path)
        save2Disk()
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

    private var cacheFile: File? = null
        get() {
            if (field == null) field = cacheFilePath?.let { File(it) }
            return field
        }

    private fun restore(savedInstanceState: Bundle): PathRecorder? {
        val loadFromDiskCache = savedInstanceState.getBoolean(BUNDLE_PATH_FROM_DISK)
        if (!loadFromDiskCache) return null

        val cf = cacheFile ?: return null
        val objInput = ObjectInputStream(FileInputStream(cf))
        return objInput.use {
            try {
                (it.readObject() as? PathRecorder)?.also { pr ->
                    pr.pathIn(PathInfo.TIP_RECOVER_FROM_SAVED_INSTANCE)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                null
            }
        }
    }


    internal fun saveInstance(outState: Bundle) {
        outState.putBoolean(BUNDLE_PATH_FROM_DISK, true)
    }

    private val savingHandler = Handler(
        HandlerThread("PathRecorder").let {
            it.start()
            it.looper
        })

    private fun save2Disk() {
        val pr = pathRecorder ?: return
        val cf = cacheFile ?: return
        savingHandler.removeCallbacksAndMessages(null)
        savingHandler.post {
            val objInput = ObjectOutputStream(FileOutputStream(cf))
            objInput.use {
                try {
                    it.writeObject(pr)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }
}