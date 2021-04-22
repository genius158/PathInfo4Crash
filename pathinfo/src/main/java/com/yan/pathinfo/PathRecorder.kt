package com.yan.pathinfo

import java.io.Serializable

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/3/15
 */
class PathRecorder : Serializable {
    companion object {
        private const val serialVersionUID: Long = 0L
    }

    val pathInfoList = ArrayList<String>()

    fun pathIn(path: String) {
        pathInfoList.add(path)
    }

    fun pathOut(path: String) {
        pathInfoList.add(path)
    }

    private var pathBuilder = StringBuilder()

    override fun toString(): String {
        val pb = pathBuilder
//        pb.clear()
        pb.delete(0,pb.length)
        val iterator = pathInfoList.iterator()
        while (iterator.hasNext()) {
            val path = iterator.next()
            pb.append(path).append("\n")
        }
        return pb.toString()
    }

}