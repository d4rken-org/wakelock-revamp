package eu.thedarken.wldonate.main.core.widget


import dagger.Subcomponent
import eu.thedarken.wldonate.common.mvpbakery.injection.broadcastreceiver.BroadcastReceiverComponent

@ToggleWidgetComponent.Scope
@Subcomponent
interface ToggleWidgetComponent : BroadcastReceiverComponent<ToggleWidgetProvider> {

    @Subcomponent.Builder
    abstract class Builder : BroadcastReceiverComponent.Builder<ToggleWidgetProvider, ToggleWidgetComponent>()

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope
}
