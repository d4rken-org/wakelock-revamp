package eu.thedarken.wldonate.main.ui


import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import eu.darken.mvpbakery.injection.PresenterComponent
import eu.darken.mvpbakery.injection.activity.ActivityComponent
import eu.darken.mvpbakery.injection.fragment.FragmentKey
import eu.thedarken.wldonate.main.ui.manager.ManagerFragment
import eu.thedarken.wldonate.main.ui.manager.ManagerFragmentComponent
import eu.thedarken.wldonate.main.ui.onboarding.OnboardingFragment
import eu.thedarken.wldonate.main.ui.onboarding.OnboardingFragmentComponent
import eu.thedarken.wldonate.main.ui.settings.SettingsFragment
import eu.thedarken.wldonate.main.ui.settings.SettingsFragmentComponent

@MainActivityComponent.Scope
@Subcomponent(modules = arrayOf(MainActivityComponent.FragmentBinderModule::class))
interface MainActivityComponent : ActivityComponent<MainActivity>, PresenterComponent<MainActivityPresenter.View, MainActivityPresenter> {

    @Subcomponent.Builder
    abstract class Builder : ActivityComponent.Builder<MainActivity, MainActivityComponent>()

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Module(subcomponents = [OnboardingFragmentComponent::class, ManagerFragmentComponent::class, SettingsFragmentComponent::class])
    abstract class FragmentBinderModule {

        @Binds
        @IntoMap
        @FragmentKey(OnboardingFragment::class)
        internal abstract fun onboarding(impl: OnboardingFragmentComponent.Builder): AndroidInjector.Factory<out Fragment>

        @Binds
        @IntoMap
        @FragmentKey(ManagerFragment::class)
        internal abstract fun manager(impl: ManagerFragmentComponent.Builder): AndroidInjector.Factory<out Fragment>

        @Binds
        @IntoMap
        @FragmentKey(SettingsFragment::class)
        internal abstract fun settings(impl: SettingsFragmentComponent.Builder): AndroidInjector.Factory<out Fragment>
    }
}
