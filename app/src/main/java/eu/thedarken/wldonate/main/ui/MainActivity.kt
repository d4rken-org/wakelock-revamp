package eu.thedarken.wldonate.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import eu.thedarken.wldonate.R
import eu.thedarken.wldonate.common.mvpbakery.base.MVPBakery
import eu.thedarken.wldonate.common.mvpbakery.base.PresenterRetainer
import eu.thedarken.wldonate.common.mvpbakery.base.ViewModelRetainer
import eu.thedarken.wldonate.common.mvpbakery.injection.ComponentSource
import eu.thedarken.wldonate.common.mvpbakery.injection.InjectedPresenter
import eu.thedarken.wldonate.common.mvpbakery.injection.ManualInjector
import eu.thedarken.wldonate.common.mvpbakery.injection.fragment.HasManualFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainActivityPresenter.View, HasManualFragmentInjector {

    @Inject lateinit var fragmentInjector: ComponentSource<Fragment>
    @Inject lateinit var navigator: Navigator

    override fun supportFragmentInjector(): ManualInjector<Fragment> = fragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.BaseAppTheme_NoActionBar)
        super.onCreate(savedInstanceState)

        MVPBakery.builder<MainActivityPresenter.View, MainActivityPresenter>()
                .presenterFactory(InjectedPresenter(this))
                .presenterRetainer(ViewModelRetainer(this))
                .addPresenterCallback(object : PresenterRetainer.Callback<MainActivityPresenter.View, MainActivityPresenter> {
                    override fun onPresenterAvailable(presenter: MainActivityPresenter) {
                        val component = presenter.component
                        component.inject(this@MainActivity)
                        navigator.mainActivity = this@MainActivity
                    }
                })
                .attach(this)

        setContentView(R.layout.main_activity)
        ButterKnife.bind(this)
    }

    override fun onDestroy() {
        navigator.mainActivity = null
        super.onDestroy()
    }
}
