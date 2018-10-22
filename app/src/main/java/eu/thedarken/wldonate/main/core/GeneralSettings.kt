package eu.thedarken.wldonate.main.core

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager
import eu.thedarken.wldonate.AppComponent
import eu.thedarken.wldonate.ApplicationContext
import eu.thedarken.wldonate.main.core.locks.Lock
import timber.log.Timber
import javax.inject.Inject

@AppComponent.Scope
class GeneralSettings @Inject constructor(@ApplicationContext val context: Context) {
    companion object {
        const val PREF_NAME = "settings_core"
        const val PREF_KEY_BUGTRACKING_ENABLED = "core.bugtracking.enabled"
        const val PREF_KEY_SHOW_ONBOARDING = "core.onboarding.show"
        const val PREF_KEY_LEGACYUSER = "core.legacyuser"
        const val PREF_KEY_SAVED_LOCKS = "core.locks.saved"
        const val PREF_KEY_AUTOSTART_BOOT = "core.autostart.boot"
        const val PREF_KEY_AUTOSTART_CALL = "core.autostart.call"
        const val PREF_KEY_ACTIVE = "core.paused"  // Shitty key name, using it inverted...
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    init {
        val legacyPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        if (legacyPrefs.contains("current_lock")) {
            preferences.edit().putBoolean(PREF_KEY_LEGACYUSER, true).apply()
        }
    }

    fun isShowOnboarding(): Boolean {
        return preferences.getBoolean(PREF_KEY_SHOW_ONBOARDING, true)
    }

    fun setOnboardingComplete(done: Boolean) {
        preferences.edit().putBoolean(PREF_KEY_SHOW_ONBOARDING, !done).apply()
    }

    fun isLegacyUser(): Boolean {
        return preferences.getBoolean(PREF_KEY_LEGACYUSER, true)
    }

    fun getSavedLocks(): HashSet<Lock.Type> {
        val lockSet = HashSet<Lock.Type>()
        for (s in preferences.getStringSet(PREF_KEY_SAVED_LOCKS, HashSet<String>())!!) {
            lockSet.add(Lock.Type.valueOf(s))
        }
        Timber.d("Loaded locks %s", lockSet)
        return lockSet
    }

    fun saveLocks(toSave: Collection<Lock.Type>) {
        Timber.d("Saving locks %s", toSave)
        val stringSet = HashSet<String>()
        toSave.forEach { stringSet.add(it.name) }
        preferences.edit().putStringSet(PREF_KEY_SAVED_LOCKS, stringSet).apply()
    }

    fun setActive(active: Boolean) {
        preferences.edit().putBoolean(PREF_KEY_ACTIVE, active).apply()
    }

    fun isActive(): Boolean {
        return preferences.getBoolean(PREF_KEY_ACTIVE, false)
    }

    fun isAutostartBootEnabled(): Boolean {
        return preferences.getBoolean(PREF_KEY_AUTOSTART_BOOT, false)
    }

    fun isAutostartCallEnabled(): Boolean {
        return preferences.getBoolean(PREF_KEY_AUTOSTART_CALL, false)
    }

    fun isBugTrackingEnabled(): Boolean {
        return preferences.getBoolean(PREF_KEY_BUGTRACKING_ENABLED, true)
    }
}