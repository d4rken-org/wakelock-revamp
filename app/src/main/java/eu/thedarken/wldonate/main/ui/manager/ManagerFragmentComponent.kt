package eu.thedarken.wldonate.main.ui.manager

import dagger.Subcomponent
import eu.darken.mvpbakery.injection.PresenterComponent
import eu.darken.mvpbakery.injection.fragment.FragmentComponent


@ManagerFragmentComponent.Scope
@Subcomponent
interface ManagerFragmentComponent : PresenterComponent<ManagerFragmentPresenter.View, ManagerFragmentPresenter>, FragmentComponent<ManagerFragment> {
    @Subcomponent.Builder
    abstract class Builder : FragmentComponent.Builder<ManagerFragment, ManagerFragmentComponent>()

    @MustBeDocumented
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope
}
