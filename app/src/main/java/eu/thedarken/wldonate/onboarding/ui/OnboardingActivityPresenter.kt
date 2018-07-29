package eu.thedarken.wldonate.main.ui

import eu.darken.mvpbakery.base.Presenter
import eu.darken.mvpbakery.injection.ComponentPresenter
import javax.inject.Inject

class OnboardingActivityPresenter @Inject
constructor(private val someRepo: SomeRepo) : ComponentPresenter<OnboardingActivityPresenter.View, OnboardingActivityComponent>() {

    override fun onBindChange(view: View?) {
        super.onBindChange(view)
        onView { v ->
            if (someRepo.isShowFragment) {
                v.showExampleFragment()
            }
        }
    }

    interface View : Presenter.View {
        fun showExampleFragment()
    }
}
