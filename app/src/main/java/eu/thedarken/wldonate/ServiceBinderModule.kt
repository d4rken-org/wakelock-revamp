package eu.thedarken.wldonate

import android.app.Service

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap
import eu.thedarken.wldonate.main.core.service.ExampleService
import eu.thedarken.wldonate.main.core.service.ExampleServiceComponent

@Module(subcomponents = arrayOf(ExampleServiceComponent::class))
internal abstract class ServiceBinderModule {

    @Binds
    @IntoMap
    @ServiceKey(ExampleService::class)
    internal abstract fun exampleService(impl: ExampleServiceComponent.Builder): AndroidInjector.Factory<out Service>
}