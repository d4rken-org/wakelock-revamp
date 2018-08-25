package eu.thedarken.wldonate

import android.content.BroadcastReceiver

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.BroadcastReceiverKey
import dagger.multibindings.IntoMap
import eu.thedarken.wldonate.main.core.receiver.LockCommandReceiver
import eu.thedarken.wldonate.main.core.receiver.LockCommandReceiverComponent

@Module(subcomponents = arrayOf(LockCommandReceiverComponent::class))
internal abstract class ReceiverBinderModule {

    @Binds
    @IntoMap
    @BroadcastReceiverKey(LockCommandReceiver::class)
    internal abstract fun exampleReceiver(impl: LockCommandReceiverComponent.Builder): AndroidInjector.Factory<out BroadcastReceiver>

}