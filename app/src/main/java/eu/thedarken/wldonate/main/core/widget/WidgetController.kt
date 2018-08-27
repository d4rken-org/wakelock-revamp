package eu.thedarken.wldonate.main.core.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import eu.thedarken.wldonate.AppComponent
import eu.thedarken.wldonate.ApplicationContext
import eu.thedarken.wldonate.main.core.locks.LockController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AppComponent.Scope
class WidgetController @Inject constructor(
        @ApplicationContext val context: Context,
        lockController: LockController
) {
    init {
        lockController.locksPub
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val intent = Intent(context, ToggleWidgetProvider::class.java)
                    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    val ids = AppWidgetManager.getInstance(context).getAppWidgetIds((ComponentName(context, ToggleWidgetProvider::class.java)))
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                    context.sendBroadcast(intent)
                }
    }
}