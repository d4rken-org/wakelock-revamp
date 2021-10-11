package eu.thedarken.wldonate.common.mvpbakery.injection.fragment

import androidx.fragment.app.Fragment
import dagger.android.support.HasSupportFragmentInjector
import eu.thedarken.wldonate.common.mvpbakery.injection.ManualInjector

interface HasManualFragmentInjector : HasSupportFragmentInjector {
    override fun supportFragmentInjector(): ManualInjector<Fragment>?
}
