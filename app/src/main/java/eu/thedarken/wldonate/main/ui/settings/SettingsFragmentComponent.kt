package eu.thedarken.wldonate.main.ui.settings


import dagger.Subcomponent
import eu.darken.mvpbakery.injection.PresenterComponent
import eu.darken.mvpbakery.injection.fragment.FragmentComponent

@SettingsFragmentComponent.Scope
@Subcomponent()
interface SettingsFragmentComponent : FragmentComponent<SettingsFragment>, PresenterComponent<SettingsFragmentPresenter.View, SettingsFragmentPresenter> {

    @Subcomponent.Builder
    abstract class Builder : FragmentComponent.Builder<SettingsFragment, SettingsFragmentComponent>()

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

}
