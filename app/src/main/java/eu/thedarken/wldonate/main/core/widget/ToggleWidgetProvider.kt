package eu.thedarken.wldonate.main.core.widget


import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.content.res.AppCompatResources
import android.util.DisplayMetrics
import android.view.View
import android.widget.RemoteViews
import eu.darken.mvpbakery.injection.broadcastreceiver.HasManualBroadcastReceiverInjector
import eu.thedarken.wldonate.R
import eu.thedarken.wldonate.common.ApiHelper
import eu.thedarken.wldonate.main.core.WidgetSettingsRepo
import eu.thedarken.wldonate.main.core.locks.Lock
import eu.thedarken.wldonate.main.core.locks.LockController
import eu.thedarken.wldonate.main.core.receiver.LockCommandReceiver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class ToggleWidgetProvider : AppWidgetProvider() {
    companion object {
        const val PI_ID_TOGGLE = 10000
        const val UPDATE_ACTION = "eu.thedarken.wldonate.UPDATE_WIDGET"
    }

    @Inject lateinit var widgetSettingsRepo: WidgetSettingsRepo
    @Inject lateinit var lockController: LockController

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { updateAppWidget(context, appWidgetManager, it, null) }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as HasManualBroadcastReceiverInjector).broadcastReceiverInjector().inject(this)

        if (intent.action == UPDATE_ACTION) {
            val man = AppWidgetManager.getInstance(context)
            val widgetComponent = ComponentName(context, ToggleWidgetProvider::class.java)
            val appWidgetIds = man.getAppWidgetIds(widgetComponent)
            onUpdate(context, man, appWidgetIds)
        } else super.onReceive(context, intent)
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle) {
        updateAppWidget(context, appWidgetManager, appWidgetId, newOptions)
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, _options: Bundle?) {
        val options = when (_options) {
            null -> appWidgetManager.getAppWidgetOptions(appWidgetId)
            else -> _options
        }

        val maxWidth: Int = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
        val maxHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)

        val toggleIntent = Intent(context, LockCommandReceiver::class.java)
        toggleIntent.action = LockCommandReceiver.ACTION_TOGGLE
        val togglePI = PendingIntent.getBroadcast(context, PI_ID_TOGGLE, toggleIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (maxWidth == 0 || maxWidth == 0) {
            Timber.d("Size not available yet, skip layouting")
            return
        }

        val rootView = RemoteViews(context.packageName, R.layout.toggle_widget_layout)

        rootView.setOnClickPendingIntent(R.id.widget, togglePI)

        var async: PendingResult? = null
        lockController.locksPub
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { async = goAsync() }
                .compose { Lock.acquiredOnly(it) }.map { !it.isEmpty() }
                .firstOrError()
                .doFinally { async?.finish() }
                .subscribe { hasLocks ->
                    if (ApiHelper.hasLolliPop()) {
                        rootView.setImageViewResource(R.id.widget_background, R.drawable.ic_widget_background)
                        rootView.setImageViewResource(R.id.widget_bulb, R.drawable.ic_widget_bulb)

                        rootView.setViewVisibility(R.id.widget_bulb, if (hasLocks) View.VISIBLE else View.GONE)
                    } else {
                        rootView.setViewVisibility(R.id.widget_bulb, View.GONE)
                        // This reduces peak memory consumption, prevents:
                        // java.lang.IllegalArgumentException: RemoteViews for widget update exceeds maximum bitmap memory usage (used: 19294272, max: 12441600)
                        //  The total memory cannot exceed that required to fill the device's screen once.
                        val drawArray = ArrayList<Int>(arrayListOf(R.drawable.ic_widget_background))
                        if (hasLocks) drawArray.add(R.drawable.ic_widget_bulb)
                        rootView.setImageViewBitmap(R.id.widget_background, bitmapIt(context, maxWidth, maxHeight, drawArray))
                    }
                    AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, rootView)
                }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        appWidgetIds.forEach { widgetSettingsRepo.deleteSettings(it) }
        super.onDeleted(context, appWidgetIds)
    }

    private fun bitmapIt(context: Context, width: Int, height: Int, @DrawableRes resVectors: ArrayList<Int>): Bitmap {
        val bitmap = Bitmap.createBitmap(dpToPixel(context, width), dpToPixel(context, height), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        resVectors.forEach {
            val drawable: Drawable = AppCompatResources.getDrawable(context, it)!!
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
        }
        return bitmap
    }

    private fun dpToPixel(context: Context, dp: Int): Int =
            (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}
