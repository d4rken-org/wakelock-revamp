package eu.thedarken.wldonate.main.ui.onboarding


import dagger.Subcomponent
import eu.thedarken.wldonate.common.mvpbakery.injection.PresenterComponent
import eu.thedarken.wldonate.common.mvpbakery.injection.fragment.FragmentComponent

@OnboardingFragmentComponent.Scope
@Subcomponent
interface OnboardingFragmentComponent : FragmentComponent<OnboardingFragment>, PresenterComponent<OnboardingFragmentPresenter, OnboardingFragmentComponent> {

    @Subcomponent.Builder
    abstract class Builder : FragmentComponent.Builder<OnboardingFragment, OnboardingFragmentComponent>()

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

}
