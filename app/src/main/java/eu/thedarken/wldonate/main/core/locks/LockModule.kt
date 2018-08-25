package eu.thedarken.wldonate.main.core.locks

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import eu.thedarken.wldonate.AppComponent


@Module
abstract class LockModule {

    @AppComponent.Scope
    @Binds @IntoMap
    @TypeKey(Lock.Type.WIFI_MODE_SCAN_ONLY)
    internal abstract fun wifiScan(lock: WifiScanLock): Lock

    @AppComponent.Scope
    @Binds @IntoMap
    @TypeKey(Lock.Type.WIFI_MODE_FULL_HIGH_PERF)
    internal abstract fun wifiFullPerf(lock: WifiFullPerfLock): Lock

    @AppComponent.Scope
    @Binds @IntoMap
    @TypeKey(Lock.Type.WIFI_MODE_FULL)
    internal abstract fun wifiFull(lock: WifiFullLock): Lock

    @AppComponent.Scope
    @Binds @IntoMap
    @TypeKey(Lock.Type.PARTIAL_WAKE_LOCK)
    internal abstract fun partial(lock: PartialWakeLock): Lock

    @AppComponent.Scope
    @Binds @IntoMap
    @TypeKey(Lock.Type.SCREEN_DIM_WAKE_LOCK)
    internal abstract fun screenDim(lock: ScreenDimWakeLock): Lock

    @AppComponent.Scope
    @Binds @IntoMap
    @TypeKey(Lock.Type.SCREEN_BRIGHT_WAKE_LOCK)
    internal abstract fun screenFull(lock: ScreenBrightWakeLock): Lock

    @AppComponent.Scope
    @Binds @IntoMap
    @TypeKey(Lock.Type.FULL_WAKE_LOCK)
    internal abstract fun full(lock: FullWakeLock): Lock
}