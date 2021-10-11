package eu.thedarken.wldonate.common.mvpbakery.injection.activity

import android.app.Activity

import dagger.android.HasActivityInjector
import eu.thedarken.wldonate.common.mvpbakery.injection.ManualInjector

interface HasManualActivityInjector : HasActivityInjector {

    override fun activityInjector(): ManualInjector<Activity>
}
