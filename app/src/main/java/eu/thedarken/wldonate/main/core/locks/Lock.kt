package eu.thedarken.wldonate.main.core.locks

import android.content.Context
import io.reactivex.Observable

interface Lock {
    enum class Type {
        WIFI_MODE_SCAN_ONLY,
        WIFI_MODE_FULL,
        WIFI_MODE_FULL_HIGH_PERF,
        PARTIAL_WAKE_LOCK,
        SCREEN_DIM_WAKE_LOCK,
        SCREEN_BRIGHT_WAKE_LOCK,
        FULL_WAKE_LOCK
    }

    fun getType(): Type

    fun acquire()

    fun release()

    fun isAcquired(): Boolean

    fun getLabel(context: Context): String

    fun getDescription(context: Context): String

    companion object {
        fun acquiredOnly(observable: Observable<Map<Type, Lock>>): Observable<Map<Type, Lock>> = observable.map { lockMap ->
            val activeOnly = HashMap<Type, Lock>()
            lockMap.values.forEach { if (it.isAcquired()) activeOnly[it.getType()] = it }
            return@map activeOnly
        }
    }
}