package eu.thedarken.wldonate.main.core.locks

import android.annotation.SuppressLint
import android.content.Context
import android.os.PowerManager
import eu.thedarken.wldonate.R
import javax.inject.Inject

class PartialWakeLock @Inject constructor(private val powerManager: PowerManager) : Lock {
    private var lock: PowerManager.WakeLock? = null

    override fun getType(): Lock.Type {
        return Lock.Type.PARTIAL_WAKE_LOCK
    }

    @SuppressLint("WakelockTimeout")
    @Synchronized
    override fun acquire() {
        if (lock == null) {
            lock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, PartialWakeLock::class.java.name)
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
        return context.getString(R.string.label_wakelock_partial)
    }

    override fun getDescription(context: Context): String {
        return context.getString(R.string.description_wakelock_partial)
    }
}