package com.yan.pathinfo

import android.os.Handler
import android.os.HandlerThread
import com.yan.pathinfo.PathInfo.TIP_RECOVER_FROM_SAVED_INSTANCE
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/3/16
 */
class RecorderCache(private var cacheFilePath: String) {
    private val charset = Charsets.US_ASCII

    private var cacheFile: File? = null
        get() {
            if (field == null) field = File(cacheFilePath)
            return field
        }

    private val savingHandler = Handler(
        HandlerThread("PathRecorder").let {
            it.start()
            it.looper
        })

    fun getCacheRecorder(): PathRecorder? {
        val cf = cacheFile ?: return null

        val pr = PathRecorder()
        val cacheReader = cf.reader(charset)
        val dataLines = try {
            cacheReader.useLines { it.toList() }
        } catch (ignore: Throwable) {
            arrayListOf<String>()
        }

        pr.pathInfoList.addAll(dataLines)

        // 崩溃恢复、内存不足被系统回收恢复
        pr.pathInfoList.add(TIP_RECOVER_FROM_SAVED_INSTANCE)

        cache2Disk(TIP_RECOVER_FROM_SAVED_INSTANCE)
        return pr
    }

    private var cacheWriter: BufferedWriter? = null

    fun cache2Disk(data: String?) {
        data ?: return
        val cf = cacheFile ?: return
        savingHandler.post {
            try {
                if (cacheWriter == null) {
                    cacheWriter = FileOutputStream(cf, true).bufferedWriter(charset)
                }
                cacheWriter
                    ?.append(data)
                    ?.append("\n")
                    ?.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun clear() {
        val cf = cacheFile ?: return

        savingHandler.post {
            try {
                cf.writeText("", charset)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}