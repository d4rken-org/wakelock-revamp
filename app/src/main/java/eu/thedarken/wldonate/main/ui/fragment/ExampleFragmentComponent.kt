package eu.thedarken.wldonate.main.ui.fragment

import dagger.Subcomponent
import eu.darken.mvpbakery.injection.PresenterComponent
import eu.darken.mvpbakery.injection.fragment.FragmentComponent


@ExampleFragmentComponent.Scope
@Subcomponent
interface ExampleFragmentComponent : PresenterComponent<ExampleFragmentPresenter.View, ExampleFragmentPresenter>, FragmentComponent<ExampleFragment> {
    @Subcomponent.Builder
    abstract class Builder : FragmentComponent.Builder<ExampleFragment, ExampleFragmentComponent>()

    @MustBeDocumented
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope
}
