package eu.thedarken.wldonate.main.core

import android.content.Context
import eu.thedarken.wldonate.AppComponent
import javax.inject.Inject

@AppComponent.Scope
class GeneralSettings @Inject constructor(val context: Context) {
    companion object {
        const val PREF_KEY_SHOW_ONBOARDING = "settings_core"
    }

    val preferences = context.getSharedPreferences("settings_core", Context.MODE_PRIVATE)

    fun showOnboarding(): Boolean {
        return preferences.getBoolean(PREF_KEY_SHOW_ONBOARDING, true)
    }

    fun setOnboardingDone(done: Boolean) {
        preferences.edit().putBoolean(PREF_KEY_SHOW_ONBOARDING, done).apply()
    }
}