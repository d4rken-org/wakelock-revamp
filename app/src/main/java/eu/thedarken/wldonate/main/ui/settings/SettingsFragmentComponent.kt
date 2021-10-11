package eu.thedarken.wldonate.main.ui.settings


import dagger.Subcomponent
import eu.thedarken.wldonate.common.mvpbakery.injection.PresenterComponent
import eu.thedarken.wldonate.common.mvpbakery.injection.fragment.FragmentComponent

@SettingsFragmentComponent.Scope
@Subcomponent
interface SettingsFragmentComponent : FragmentComponent<SettingsFragment>, PresenterComponent<SettingsFragmentPresenter, SettingsFragmentComponent> {

    @Subcomponent.Builder
    abstract class Builder : FragmentComponent.Builder<SettingsFragment, SettingsFragmentComponent>()

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

}
