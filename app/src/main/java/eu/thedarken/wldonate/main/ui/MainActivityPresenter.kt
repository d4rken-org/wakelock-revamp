package eu.thedarken.wldonate.main.ui

import eu.thedarken.wldonate.common.mvpbakery.base.Presenter
import eu.thedarken.wldonate.common.mvpbakery.injection.ComponentPresenter
import eu.thedarken.wldonate.main.core.GeneralSettings
import javax.inject.Inject

class MainActivityPresenter @Inject
constructor(
        private val settings: GeneralSettings,
        private val navigator: Navigator
) : ComponentPresenter<MainActivityPresenter.View, MainActivityComponent>() {
    private var initialLaunch: Boolean = true

    override fun onBindChange(view: View?) {
        super.onBindChange(view)
        if (initialLaunch) {
            initialLaunch = false
            withView {
                when {
                    settings.isShowOnboarding() -> navigator.goToOnboarding()
                    else -> navigator.goToManagement()
                }
            }
        }
    }

    interface View : Presenter.View
}
