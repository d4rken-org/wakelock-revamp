package eu.thedarken.wldonate.main.core.service

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import eu.thedarken.wldonate.AppComponent
import eu.thedarken.wldonate.ApplicationContext
import eu.thedarken.wldonate.main.core.locks.Lock
import eu.thedarken.wldonate.main.core.locks.LockController
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AppComponent.Scope
class ServiceController @Inject constructor(
        @ApplicationContext val context: Context,
        lockController: LockController
) {
    init {
        lockController.locksPub
                .subscribeOn(Schedulers.computation())
                .compose { Lock.acquiredOnly(it) }
                .throttleLast(1000, TimeUnit.MILLISECONDS)
                .subscribe { it ->
                    if (it.isEmpty()) {
                        stopService()
                    } else {
                        startService()
                    }
                }
    }

    val intent: Intent = Intent(context, LockService::class.java)
    var serviceStarted: Boolean = false

    private fun startService() {
        if (serviceStarted) return
        serviceStarted = true
        Timber.d("Starting service")
        ContextCompat.startForegroundService(context, intent)
    }

    private fun stopService() {
        if (!serviceStarted) return
        serviceStarted = false
        Timber.d("Stopping service")
        context.stopService(intent)
    }

}