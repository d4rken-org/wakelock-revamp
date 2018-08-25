package eu.thedarken.wldonate.main.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import eu.darken.mvpbakery.injection.broadcastreceiver.HasManualBroadcastReceiverInjector
import eu.thedarken.wldonate.main.core.GeneralSettings
import eu.thedarken.wldonate.main.core.locks.LockController
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class LockCommandReceiver : BroadcastReceiver() {
    companion object {
        @JvmStatic val ACTION_EXIT = "RELEASE_ALL_LOCKS"
    }

    @Inject lateinit var lockController: LockController
    @Inject lateinit var settings: GeneralSettings

    override fun onReceive(context: Context, intent: Intent) {
        Timber.v("onReceive(%s, %s)", context, intent)

        if (context.applicationContext !is HasManualBroadcastReceiverInjector) {
            Timber.e("Some weird ROM, 6.0/7.0 Samsung?")
            return
        }

        (context.applicationContext as HasManualBroadcastReceiverInjector).broadcastReceiverInjector().inject(this)

        if (intent.action == null) {
            Timber.w("Intent with unknown action")
            return
        }

        if (intent.action!! == ACTION_EXIT) {
            Timber.i("Manual exit...")
            releaseAll()
        } else if (intent.action!! == Intent.ACTION_BOOT_COMPLETED && settings.isAutostartBootEnabled() && !settings.isAutostartCallEnabled()) {
            Timber.i("Reboot, restoring locks...")
            acquireSaved()
        } else if (intent.action!! == TelephonyManager.ACTION_PHONE_STATE_CHANGED && settings.isAutostartCallEnabled()) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (TelephonyManager.EXTRA_STATE_RINGING == state || TelephonyManager.EXTRA_STATE_OFFHOOK == state) {
                Timber.i("Call incoming...")
                acquireSaved()
            } else if (TelephonyManager.EXTRA_STATE_IDLE == state) {
                Timber.i("Call ended, stopping it...")
                releaseAll()
            }
        }
    }

    private fun releaseAll() {
        val async = goAsync()
        lockController.acquireExclusive(Collections.emptySet())
                .subscribeOn(Schedulers.computation())
                .doFinally { async.finish() }
                .subscribe()
    }

    private fun acquireSaved() {
        val async = goAsync()
        val desired = settings.getSavedLocks()
        lockController.acquireExclusive(desired)
                .subscribeOn(Schedulers.computation())
                .doFinally { async.finish() }
                .subscribe()
    }
}
