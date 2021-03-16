package com.yan.pathinfo

import android.os.Handler
import android.os.HandlerThread
import java.io.*

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/3/16
 */
class RecorderCache(private var cacheFilePath: String) {

    private var cacheFile: File? = null
        get() {
            if (field == null) field = File(cacheFilePath)
            return field
        }

    fun getRecorder(): PathRecorder? {
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

    private val savingHandler = Handler(
        HandlerThread("PathRecorder").let {
            it.start()
            it.looper
        })

    /**
     * 专门用来写入文件的PathRecorder
     * 子线程操作
     * 处理ArrayList 写入是可能抛ConcurrentModificationException
     */
    private val writeRecorder = PathRecorder()
    fun cache2Disk(pathRecorder: PathRecorder?) {
        val pr = pathRecorder ?: return
        val cf = cacheFile ?: return
        savingHandler.removeCallbacksAndMessages(null)
        savingHandler.post {
            val objInput = ObjectOutputStream(FileOutputStream(cf))
            objInput.use {
                try {
                    writeRecorder.pathInfoList.clear()
                    writeRecorder.pathInfoList.addAll(pr.pathInfoList)
                    it.writeObject(writeRecorder)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }
}