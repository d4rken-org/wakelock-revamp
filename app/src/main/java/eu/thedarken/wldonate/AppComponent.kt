package eu.thedarken.wldonate

import android.app.Activity
import dagger.Component
import dagger.MembersInjector
import eu.thedarken.wldonate.common.mvpbakery.injection.ComponentSource
import eu.thedarken.wldonate.main.core.locks.LockModule


@AppComponent.Scope
@Component(modules = arrayOf(
        AndroidModule::class,
        ActivityBinderModule::class,
        ServiceBinderModule::class,
        ReceiverBinderModule::class,
        LockModule::class
))
interface AppComponent : MembersInjector<App> {
    fun inject(app: App)

    fun activityInjector(): ComponentSource<Activity>

    @Component.Builder
    interface Builder {
        fun androidModule(module: AndroidModule): Builder

        fun build(): AppComponent
    }

    @MustBeDocumented
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope
}
