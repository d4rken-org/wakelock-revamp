package eu.thedarken.wldonate.main.ui.onboarding

import eu.thedarken.wldonate.common.mvpbakery.base.Presenter
import eu.thedarken.wldonate.common.mvpbakery.injection.ComponentPresenter
import eu.thedarken.wldonate.main.core.GeneralSettings
import eu.thedarken.wldonate.main.ui.Navigator
import javax.inject.Inject

@OnboardingFragmentComponent.Scope
class OnboardingFragmentPresenter @Inject constructor(
        val navigator: Navigator,
        val settings: GeneralSettings
) : ComponentPresenter<OnboardingFragmentPresenter.View, OnboardingFragmentComponent>() {

    private var showingLegacyExplanation: Boolean = false

    override fun onBindChange(view: View?) {
        super.onBindChange(view)
        if (settings.isLegacyUser()) {
            showingLegacyExplanation = true
            withView { v -> v.showLegacyExplanation(true) }
        }
    }

    fun onFinishOnboarding() {
        settings.setOnboardingComplete(true)
        navigator.goToManagement()
    }

    fun onDownloadLegacy() {
        navigator.goToLegacyVersion()
    }

    interface View : Presenter.View {
        fun showLegacyExplanation(show: Boolean)
    }
}
