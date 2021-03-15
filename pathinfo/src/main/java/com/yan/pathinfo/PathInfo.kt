package com.yan.pathinfo

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/3/15
 */
internal object PathInfo {
    const val TIP_RECOVER_FROM_SAVED_INSTANCE: String = "----RecoverFromSavedInstance----"

    fun activity(act: Any, ain: Boolean): String {
        if (ain) return ">>>ACTIVITY:${act.javaClass.name}"

        return "<<<ACTIVITY:${act.javaClass.name}"
    }

    fun fragment(act: Any, fin: Boolean): String {
        if (fin) return "->FRAGMENT:${act.javaClass.name}"

        return "<-FRAGMENT:${act.javaClass.name}"
    }
}
