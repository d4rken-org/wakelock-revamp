package eu.thedarken.wldonate.main.core.service

import dagger.Subcomponent
import eu.darken.mvpbakery.injection.service.ServiceComponent


@ExampleServiceComponent.Scope
@Subcomponent
interface ExampleServiceComponent : ServiceComponent<ExampleService> {

    @Subcomponent.Builder
    abstract class Builder : ServiceComponent.Builder<ExampleService, ExampleServiceComponent>()

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope
}
