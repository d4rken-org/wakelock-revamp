package eu.thedarken.wldonate.common.timber

import com.bugsnag.android.Event
import com.bugsnag.android.OnErrorCallback
import eu.thedarken.wldonate.BuildConfig
import eu.thedarken.wldonate.main.core.GeneralSettings

class BugsnagErrorHandler(
        private val settings: GeneralSettings,
        private val bugsnagTree: BugsnagTree
) : OnErrorCallback {

    override fun onError(event: Event): Boolean {
        if (!settings.isBugTrackingEnabled()) return false
        bugsnagTree.injectLog(event)
        event.addMetadata(TAB_APP, "gitSha", BuildConfig.GITSHA)
        event.addMetadata(TAB_APP, "buildTime", BuildConfig.BUILDTIME)
        return !BuildConfig.DEBUG
    }

    companion object {
        private const val TAB_APP = "app"

    }

}