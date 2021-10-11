package eu.thedarken.wldonate

import android.app.Service
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import eu.thedarken.wldonate.common.mvpbakery.injection.service.ServiceKey
import eu.thedarken.wldonate.main.core.service.LockService
import eu.thedarken.wldonate.main.core.service.LockServiceComponent

@Module(subcomponents = arrayOf(LockServiceComponent::class))
internal abstract class ServiceBinderModule {

    @Binds
    @IntoMap
    @ServiceKey(LockService::class)
    internal abstract fun exampleService(impl: LockServiceComponent.Builder): AndroidInjector.Factory<out Service>
}