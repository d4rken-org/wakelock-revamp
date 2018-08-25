package eu.thedarken.wldonate.main.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import eu.darken.mvpbakery.base.MVPBakery
import eu.darken.mvpbakery.base.ViewModelRetainer
import eu.darken.mvpbakery.injection.ComponentSource
import eu.darken.mvpbakery.injection.InjectedPresenter
import eu.darken.mvpbakery.injection.ManualInjector
import eu.darken.mvpbakery.injection.fragment.HasManualFragmentInjector
import eu.thedarken.wldonate.R
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
                .addPresenterCallback {
                    val component = it.component
                    component.inject(this@MainActivity)
                    navigator.mainActivity = this@MainActivity
                }
                .attach(this)

        setContentView(R.layout.main_activity)
        ButterKnife.bind(this)
    }

    override fun onDestroy() {
        navigator.mainActivity = null
        super.onDestroy()
    }
}
