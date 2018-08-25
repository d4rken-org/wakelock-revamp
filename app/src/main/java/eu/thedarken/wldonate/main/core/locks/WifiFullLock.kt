package eu.thedarken.wldonate.main.core.locks

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import eu.thedarken.wldonate.R
import javax.inject.Inject

class WifiFullLock @Inject constructor(private val wifiManager: WifiManager) : Lock {
    private var lock: WifiManager.WifiLock? = null
    override fun getType(): Lock.Type {
        return Lock.Type.WIFI_MODE_FULL
    }

    @SuppressLint("WakelockTimeout")
    @Synchronized
    override fun acquire() {
        if (lock == null) {
            lock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, WifiFullLock::class.java.name)
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
        return context.getString(R.string.label_wifilock_full)
    }

    override fun getDescription(context: Context): String {
        return context.getString(R.string.description_wifilock_full)
    }
}