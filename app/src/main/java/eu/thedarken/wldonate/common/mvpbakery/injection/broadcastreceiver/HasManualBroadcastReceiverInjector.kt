package eu.thedarken.wldonate.common.mvpbakery.injection.broadcastreceiver


import android.content.BroadcastReceiver

import dagger.android.HasBroadcastReceiverInjector
import eu.thedarken.wldonate.common.mvpbakery.injection.ManualInjector

interface HasManualBroadcastReceiverInjector : HasBroadcastReceiverInjector {
    override fun broadcastReceiverInjector(): ManualInjector<BroadcastReceiver>
}
