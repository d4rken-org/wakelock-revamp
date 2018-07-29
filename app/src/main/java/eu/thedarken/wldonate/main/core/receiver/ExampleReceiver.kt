package eu.thedarken.wldonate.main.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import eu.darken.mvpbakery.injection.broadcastreceiver.HasManualBroadcastReceiverInjector
import timber.log.Timber

class ExampleReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.v("onReceive(%s, %s)", context, intent)
        (context.applicationContext as HasManualBroadcastReceiverInjector).broadcastReceiverInjector().inject(this)
    }
}
