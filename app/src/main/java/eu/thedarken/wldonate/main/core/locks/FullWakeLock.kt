package eu.thedarken.wldonate.main.core.locks

import android.annotation.SuppressLint
import android.content.Context
import android.os.PowerManager
import eu.thedarken.wldonate.R
import javax.inject.Inject

class FullWakeLock @Inject constructor(val powerManager: PowerManager) : Lock {
    private var lock: PowerManager.WakeLock? = null

    override fun getType(): Lock.Type {
        return Lock.Type.FULL_WAKE_LOCK
    }

    @SuppressLint("WakelockTimeout")
    @Synchronized
    override fun acquire() {
        if (lock == null) {
            @Suppress("DEPRECATION")
            lock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, FullWakeLock::class.java.name)
        }
        if (!lock!!.isHeld) lock!!.acquire()
    }

    @Synchronized
    override fun isAcquired(): Boolean {
        return lock != null && lock!!.isHeld
    }

    @Synchronized
    override fun release() {
        if (isAcquired()) lock?.release()
    }

    override fun getLabel(context: Context): String {
        return context.getString(R.string.label_wakelock_full)
    }

    override fun getDescription(context: Context): String {
        return context.getString(R.string.description_wakelock_full)
    }
}