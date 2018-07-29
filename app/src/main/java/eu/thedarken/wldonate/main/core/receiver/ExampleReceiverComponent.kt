package eu.thedarken.wldonate.main.core.receiver


import dagger.Subcomponent
import eu.darken.mvpbakery.injection.broadcastreceiver.BroadcastReceiverComponent

@ExampleReceiverComponent.Scope
@Subcomponent
interface ExampleReceiverComponent : BroadcastReceiverComponent<ExampleReceiver> {

    @Subcomponent.Builder
    abstract class Builder : BroadcastReceiverComponent.Builder<ExampleReceiver, ExampleReceiverComponent>()

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope
}
