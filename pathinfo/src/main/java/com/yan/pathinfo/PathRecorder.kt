package com.yan.pathinfo

import java.io.Serializable

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/3/15
 */
internal class PathRecorder : Serializable {
    companion object {
        private const val serialVersionUID: Long = 0L
    }

    private val pathInfoList = ArrayList<String>()

    fun pathIn(path: String) {
        pathInfoList.add(path)
    }

    fun pathOut(path: String) {
        pathInfoList.add(path)
    }

    @Transient
    private var pathBuilder: StringBuilder? = null
        get() {
            if (field == null) field = StringBuilder()
            return field
        }

    override fun toString(): String {
        pathBuilder?.clear()
        val iterator = pathInfoList.iterator()

        while (iterator.hasNext()) {
            val path = iterator.next()
            pathBuilder?.append(path)?.append("\n")
        }
        return pathBuilder.toString()
    }

    fun clone(): PathRecorder {
        return PathRecorder().also { it.pathInfoList.addAll(pathInfoList) }
    }

}