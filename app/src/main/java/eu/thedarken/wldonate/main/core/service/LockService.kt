package eu.thedarken.wldonate.main.core.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import eu.thedarken.wldonate.App
import eu.thedarken.wldonate.R
import eu.thedarken.wldonate.common.smart.SmartService
import eu.thedarken.wldonate.main.core.locks.Lock
import eu.thedarken.wldonate.main.core.locks.LockController
import eu.thedarken.wldonate.main.core.receiver.LockCommandReceiver
import eu.thedarken.wldonate.main.ui.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class LockService : SmartService() {
    companion object {
        @JvmStatic val NOTIFICATION_CHANNEL_ID: String = "core.notification.channel.status"
        @JvmStatic val NOTIFICATION_SERVICE_ID: Int = 2
        @JvmStatic val NOTIFICATION_PI_ID_OPEN_APP: Int = 3
        @JvmStatic val NOTIFICATION_PI_ID_EXIT: Int = 4
    }

    @Inject lateinit var binder: LockServiceBinder
    @Inject lateinit var notificationManager: NotificationManager
    @Inject lateinit var lockController: LockController

    private lateinit var statusNotifBuilder: NotificationCompat.Builder
    private var notifUpdateSub: Disposable = Disposables.disposed()

    override fun onCreate() {
        (application as App).serviceInjector().inject(this)
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val statusChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, getString(R.string.label_notification_channel_status), NotificationManager.IMPORTANCE_MIN)
            notificationManager.createNotificationChannel(statusChannel)
        }

        statusNotifBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_stencil_icon)
                .setAutoCancel(false)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.status_text_idle))

        val openIntent = Intent(this, MainActivity::class.java)
        val openPI = PendingIntent.getActivity(this, NOTIFICATION_PI_ID_OPEN_APP, openIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        statusNotifBuilder.setContentIntent(openPI)

        val exitIntentBroadcast = Intent(this, LockCommandReceiver::class.java)
        exitIntentBroadcast.action = "RELEASE_ALL_LOCKS"
        val exitPI = PendingIntent.getBroadcast(this, NOTIFICATION_PI_ID_EXIT, exitIntentBroadcast, PendingIntent.FLAG_UPDATE_CURRENT)
        statusNotifBuilder.addAction(NotificationCompat.Action.Builder(R.drawable.ic_pause_circle_filled_white_24dp, getString(R.string.action_pause), exitPI).build())

        startForeground(NOTIFICATION_SERVICE_ID, statusNotifBuilder.build())
        notifUpdateSub = lockController.locksPub
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    var acquired = 0
                    it.values.forEach { lock: Lock -> if (lock.isAcquired()) acquired++ }
                    statusNotifBuilder.setContentText(resources.getQuantityString(R.plurals.x_locks_acquired, acquired, acquired))
                    notificationManager.notify(NOTIFICATION_SERVICE_ID, statusNotifBuilder.build())
                }
    }

    override fun onBind(intent: Intent): IBinder {
        Timber.d("onBind(%s)", intent)
        return binder
    }

    override fun onDestroy() {
        notifUpdateSub.dispose()
        stopForeground(true)
        super.onDestroy()
    }

}