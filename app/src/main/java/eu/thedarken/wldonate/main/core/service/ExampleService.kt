package eu.thedarken.wldonate.main.core.service

import android.content.Intent
import android.os.IBinder
import eu.thedarken.wldonate.App
import eu.thedarken.wldonate.common.smart.SmartService
import javax.inject.Inject

class ExampleService : SmartService() {

    @Inject lateinit var binder: ExampleBinder

    override fun onCreate() {
        (application as App).serviceInjector().inject(this)
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder = binder

}