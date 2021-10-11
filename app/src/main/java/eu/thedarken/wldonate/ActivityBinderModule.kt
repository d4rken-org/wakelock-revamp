package eu.thedarken.wldonate

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import eu.thedarken.wldonate.common.mvpbakery.injection.activity.ActivityKey
import eu.thedarken.wldonate.main.ui.MainActivity
import eu.thedarken.wldonate.main.ui.MainActivityComponent

@Module(subcomponents = arrayOf(
        MainActivityComponent::class
))
internal abstract class ActivityBinderModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun main(impl: MainActivityComponent.Builder): AndroidInjector.Factory<out Activity>
}