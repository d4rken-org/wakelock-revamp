package eu.thedarken.wldonate.main.ui.onboarding


import dagger.Subcomponent
import eu.darken.mvpbakery.injection.PresenterComponent
import eu.darken.mvpbakery.injection.fragment.FragmentComponent

@OnboardingFragmentComponent.Scope
@Subcomponent()
interface OnboardingFragmentComponent : FragmentComponent<OnboardingFragment>, PresenterComponent<OnboardingFragmentPresenter.View, OnboardingFragmentPresenter> {

    @Subcomponent.Builder
    abstract class Builder : FragmentComponent.Builder<OnboardingFragment, OnboardingFragmentComponent>()

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

}
