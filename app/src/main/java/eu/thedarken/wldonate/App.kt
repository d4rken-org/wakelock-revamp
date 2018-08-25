package eu.thedarken.wldonate

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.BroadcastReceiver
import android.support.v7.app.AppCompatDelegate
import com.bugsnag.android.Bugsnag
import eu.darken.mvpbakery.injection.ComponentSource
import eu.darken.mvpbakery.injection.ManualInjector
import eu.darken.mvpbakery.injection.activity.HasManualActivityInjector
import eu.darken.mvpbakery.injection.broadcastreceiver.HasManualBroadcastReceiverInjector
import eu.darken.mvpbakery.injection.service.HasManualServiceInjector
import eu.thedarken.wldonate.common.UUIDToken
import eu.thedarken.wldonate.common.timber.BugsnagErrorHandler
import eu.thedarken.wldonate.common.timber.BugsnagTree
import eu.thedarken.wldonate.main.core.GeneralSettings
import eu.thedarken.wldonate.main.core.service.ServiceController
import timber.log.Timber
import javax.inject.Inject


open class App : Application(), HasManualActivityInjector, HasManualBroadcastReceiverInjector, HasManualServiceInjector {

    lateinit var activityInjector: ManualInjector<Activity>
    @Inject lateinit var appComponent: AppComponent
    @Inject lateinit var receiverInjector: ComponentSource<BroadcastReceiver>
    @Inject lateinit var serviceInjector: ComponentSource<Service>
    @Inject lateinit var serviceController: ServiceController

    @Inject lateinit var settings: GeneralSettings
    @Inject lateinit var uuidToken: UUIDToken

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        DaggerAppComponent.builder()
                .androidModule(AndroidModule(this))
                .build()
                .injectMembers(this)

        val bugsnagClient = Bugsnag.init(this)
        bugsnagClient.setUserId(uuidToken.id())

        val bugsnagTree = BugsnagTree()
        Timber.plant(bugsnagTree)
        bugsnagClient.beforeNotify(BugsnagErrorHandler(settings, bugsnagTree))
        Timber.i("Bugsnag setup done!")


        activityInjector = appComponent.activityInjector()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        Timber.d("onCreate() done!")
    }

    override fun activityInjector(): ManualInjector<Activity> {
        return activityInjector
    }

    override fun broadcastReceiverInjector(): ManualInjector<BroadcastReceiver> {
        return receiverInjector
    }

    override fun serviceInjector(): ManualInjector<Service> {
        return serviceInjector
    }

    companion object {
        internal val TAG = logTag("ExampleApp")

        fun logTag(vararg tags: String): String {
            val sb = StringBuilder()
            for (i in tags.indices) {
                sb.append(tags[i])
                if (i < tags.size - 1) sb.append(":")
            }
            return sb.toString()
        }
    }
}
