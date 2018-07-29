package eu.thedarken.wldonate.main.ui

import eu.darken.mvpbakery.base.Presenter
import eu.darken.mvpbakery.injection.ComponentPresenter
import eu.thedarken.wldonate.main.core.GeneralSettings
import javax.inject.Inject

class MainActivityPresenter @Inject
constructor(
        private val settings: GeneralSettings
) : ComponentPresenter<MainActivityPresenter.View, MainActivityComponent>() {

    override fun onBindChange(view: View?) {
        super.onBindChange(view)

        onView { v ->
            when {
                settings.showOnboarding() -> v.showOnboarding()
                else -> v.showWakelockManagement()
            }
        }

    }

    interface View : Presenter.View {
        fun showWakelockManagement()
        fun showOnboarding()
    }
}
