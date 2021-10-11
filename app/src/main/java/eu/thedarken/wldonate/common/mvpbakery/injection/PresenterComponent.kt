package eu.thedarken.wldonate.common.mvpbakery.injection

import eu.thedarken.wldonate.common.mvpbakery.base.Presenter

interface PresenterComponent<PresenterT : ComponentPresenter<out Presenter.View, Self>, Self : PresenterComponent<PresenterT, Self>> {
    val presenter: PresenterT
}
