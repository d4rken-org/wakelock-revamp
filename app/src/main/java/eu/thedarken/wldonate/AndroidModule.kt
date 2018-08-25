package eu.thedarken.wldonate

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.PowerManager
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides


@Module
class AndroidModule(private val app: App) {

    @Provides
    @AppComponent.Scope
    @ApplicationContext
    fun context(): Context = app.applicationContext

    @Provides
    @AppComponent.Scope
    fun preferences(@ApplicationContext context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @AppComponent.Scope
    fun powerManager(@ApplicationContext context: Context): PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

    @SuppressLint("WifiManagerPotentialLeak")
    @Provides
    @AppComponent.Scope
    fun wifiManager(@ApplicationContext context: Context): WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Provides
    @AppComponent.Scope
    fun notificationManager(@ApplicationContext context: Context): NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @AppComponent.Scope
    fun packageManager(@ApplicationContext context: Context): PackageManager = context.packageManager
}
