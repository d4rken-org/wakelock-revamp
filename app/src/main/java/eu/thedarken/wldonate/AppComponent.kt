package eu.thedarken.wldonate

import android.app.Activity
import dagger.Component
import dagger.MembersInjector
import eu.darken.mvpbakery.injection.ComponentSource


@AppComponent.Scope
@Component(modules = arrayOf(AndroidModule::class, ActivityBinderModule::class, ServiceBinderModule::class, ReceiverBinderModule::class))
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
