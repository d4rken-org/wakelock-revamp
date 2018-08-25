package eu.thedarken.wldonate.main.core.receiver


import dagger.Subcomponent
import eu.darken.mvpbakery.injection.broadcastreceiver.BroadcastReceiverComponent

@LockCommandReceiverComponent.Scope
@Subcomponent
interface LockCommandReceiverComponent : BroadcastReceiverComponent<LockCommandReceiver> {

    @Subcomponent.Builder
    abstract class Builder : BroadcastReceiverComponent.Builder<LockCommandReceiver, LockCommandReceiverComponent>()

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope
}
