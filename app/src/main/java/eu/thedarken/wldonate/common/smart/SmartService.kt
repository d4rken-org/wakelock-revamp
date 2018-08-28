package eu.thedarken.wldonate.common.smart

import android.app.Service
import android.content.Intent
import android.content.res.Configuration

import eu.thedarken.wldonate.App
import timber.log.Timber


abstract class SmartService : Service() {
    private val tag: String = App.logTag("Service", this.javaClass.simpleName + "(" + Integer.toHexString(this.hashCode()) + ")")

    override fun onCreate() {
        Timber.tag(tag).d("onCreate()")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag(tag).d("onStartCommand(intent=%s, flags=%s startId=%d)", intent, flags, startId)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Timber.tag(tag).d("onDestroy()")
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Timber.tag(tag).d("onConfigurationChanged(newConfig=%s)", newConfig)
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        Timber.tag(tag).d("onLowMemory()")
        super.onLowMemory()
    }

    override fun onUnbind(intent: Intent): Boolean {
        Timber.tag(tag).d("onUnbind(intent=%s)", intent)
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent) {
        Timber.tag(tag).d("onRebind(intent=%s)", intent)
        super.onRebind(intent)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        Timber.tag(tag).d("onTaskRemoved(rootIntent=%s)", rootIntent)
        super.onTaskRemoved(rootIntent)
    }
}
