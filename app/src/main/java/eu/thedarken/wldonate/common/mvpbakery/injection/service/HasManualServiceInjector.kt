package eu.thedarken.wldonate.common.mvpbakery.injection.service


import android.app.Service

import dagger.android.HasServiceInjector
import eu.thedarken.wldonate.common.mvpbakery.injection.ManualInjector

interface HasManualServiceInjector : HasServiceInjector {
    override fun serviceInjector(): ManualInjector<Service>
}
