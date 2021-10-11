package eu.thedarken.wldonate.main.core.service

import dagger.Subcomponent
import eu.thedarken.wldonate.common.mvpbakery.injection.service.ServiceComponent


@LockServiceComponent.Scope
@Subcomponent
interface LockServiceComponent : ServiceComponent<LockService> {

    @Subcomponent.Builder
    abstract class Builder : ServiceComponent.Builder<LockService, LockServiceComponent>()

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope
}
