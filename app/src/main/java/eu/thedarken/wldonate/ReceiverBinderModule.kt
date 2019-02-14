package eu.thedarken.wldonate

import android.content.BroadcastReceiver
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import eu.darken.mvpbakery.injection.broadcastreceiver.BroadcastReceiverKey
import eu.thedarken.wldonate.main.core.receiver.LockCommandReceiver
import eu.thedarken.wldonate.main.core.receiver.LockCommandReceiverComponent
import eu.thedarken.wldonate.main.core.widget.ToggleWidgetComponent
import eu.thedarken.wldonate.main.core.widget.ToggleWidgetProvider

@Module(subcomponents = arrayOf(
        LockCommandReceiverComponent::class,
        ToggleWidgetComponent::class
))
internal abstract class ReceiverBinderModule {

    @Binds
    @IntoMap
    @BroadcastReceiverKey(LockCommandReceiver::class)
    internal abstract fun exampleReceiver(impl: LockCommandReceiverComponent.Builder): AndroidInjector.Factory<out BroadcastReceiver>

    @Binds
    @IntoMap
    @BroadcastReceiverKey(ToggleWidgetProvider::class)
    internal abstract fun widgetReceiver(impl: ToggleWidgetComponent.Builder): AndroidInjector.Factory<out BroadcastReceiver>
}