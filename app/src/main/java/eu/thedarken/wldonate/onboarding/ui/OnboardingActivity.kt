package eu.thedarken.wldonate.main.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import butterknife.ButterKnife
import eu.darken.mvpbakery.base.MVPBakery
import eu.darken.mvpbakery.base.ViewModelRetainer
import eu.darken.mvpbakery.injection.ComponentSource
import eu.darken.mvpbakery.injection.InjectedPresenter
import eu.darken.mvpbakery.injection.ManualInjector
import eu.darken.mvpbakery.injection.PresenterInjectionCallback
import eu.darken.mvpbakery.injection.fragment.HasManualFragmentInjector
import eu.thedarken.wldonate.R
import eu.thedarken.wldonate.main.ui.fragment.ExampleFragment
import javax.inject.Inject

class OnboardingActivity : AppCompatActivity(), OnboardingActivityPresenter.View, HasManualFragmentInjector {

    @Inject lateinit var fragmentInjector: ComponentSource<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MVPBakery.builder<OnboardingActivityPresenter.View, OnboardingActivityPresenter>()
                .presenterFactory(InjectedPresenter(this))
                .presenterRetainer(ViewModelRetainer(this))
                .addPresenterCallback(PresenterInjectionCallback(this))
                .attach(this)

        setContentView(R.layout.onboarding_activity)
        ButterKnife.bind(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun supportFragmentInjector(): ManualInjector<Fragment> = fragmentInjector

    override fun showExampleFragment() {
        var fragment = supportFragmentManager.findFragmentById(R.id.content_frame)
        if (fragment == null) fragment = ExampleFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commitAllowingStateLoss()
    }
}
