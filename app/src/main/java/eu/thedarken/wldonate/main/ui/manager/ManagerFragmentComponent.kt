package eu.thedarken.wldonate.main.ui.manager

import dagger.Subcomponent
import eu.thedarken.wldonate.common.mvpbakery.injection.PresenterComponent
import eu.thedarken.wldonate.common.mvpbakery.injection.fragment.FragmentComponent


@ManagerFragmentComponent.Scope
@Subcomponent
interface ManagerFragmentComponent : PresenterComponent<ManagerFragmentPresenter, ManagerFragmentComponent>, FragmentComponent<ManagerFragment> {
    @Subcomponent.Builder
    abstract class Builder : FragmentComponent.Builder<ManagerFragment, ManagerFragmentComponent>()

    @MustBeDocumented
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope
}
