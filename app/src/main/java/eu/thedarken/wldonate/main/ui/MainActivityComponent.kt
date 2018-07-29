package eu.thedarken.wldonate.main.ui


import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import eu.darken.mvpbakery.injection.PresenterComponent
import eu.darken.mvpbakery.injection.activity.ActivityComponent
import eu.thedarken.wldonate.main.ui.fragment.ExampleFragment
import eu.thedarken.wldonate.main.ui.fragment.ExampleFragmentComponent

@OnboardingActivityComponent.Scope
@Subcomponent(modules = arrayOf(OnboardingActivityComponent.FragmentBinderModule::class))
interface MainActivityComponent : ActivityComponent<MainActivity>, PresenterComponent<MainActivityPresenter.View, MainActivityPresenter> {

    @Subcomponent.Builder
    abstract class Builder : ActivityComponent.Builder<MainActivity, MainActivityComponent>()

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Module(subcomponents = arrayOf(ExampleFragmentComponent::class))
    abstract class FragmentBinderModule {

        @Binds
        @IntoMap
        @FragmentKey(ExampleFragment::class)
        internal abstract fun exampleFragment(impl: ExampleFragmentComponent.Builder): AndroidInjector.Factory<out Fragment>

    }
}
