package eu.thedarken.wldonate.main.ui


import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import eu.thedarken.wldonate.common.mvpbakery.injection.PresenterComponent
import eu.thedarken.wldonate.common.mvpbakery.injection.activity.ActivityComponent
import eu.thedarken.wldonate.common.mvpbakery.injection.fragment.FragmentKey
import eu.thedarken.wldonate.main.ui.manager.ManagerFragment
import eu.thedarken.wldonate.main.ui.manager.ManagerFragmentComponent
import eu.thedarken.wldonate.main.ui.onboarding.OnboardingFragment
import eu.thedarken.wldonate.main.ui.onboarding.OnboardingFragmentComponent
import eu.thedarken.wldonate.main.ui.settings.SettingsFragment
import eu.thedarken.wldonate.main.ui.settings.SettingsFragmentComponent

@MainActivityComponent.Scope
@Subcomponent(modules = [MainActivityComponent.FragmentBinderModule::class])
interface MainActivityComponent : ActivityComponent<MainActivity>, PresenterComponent<MainActivityPresenter, MainActivityComponent> {

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
        internal abstract fun onboarding(impl: OnboardingFragmentComponent.Builder): AndroidInjector.Factory<out androidx.fragment.app.Fragment>

        @Binds
        @IntoMap
        @FragmentKey(ManagerFragment::class)
        internal abstract fun manager(impl: ManagerFragmentComponent.Builder): AndroidInjector.Factory<out androidx.fragment.app.Fragment>

        @Binds
        @IntoMap
        @FragmentKey(SettingsFragment::class)
        internal abstract fun settings(impl: SettingsFragmentComponent.Builder): AndroidInjector.Factory<out androidx.fragment.app.Fragment>
    }
}
