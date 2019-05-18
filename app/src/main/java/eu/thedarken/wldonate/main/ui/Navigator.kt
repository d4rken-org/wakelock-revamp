package eu.thedarken.wldonate.main.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import eu.thedarken.wldonate.ApplicationContext
import eu.thedarken.wldonate.R
import eu.thedarken.wldonate.main.ui.manager.ManagerFragment
import eu.thedarken.wldonate.main.ui.onboarding.OnboardingFragment
import eu.thedarken.wldonate.main.ui.settings.SettingsFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by darken(darken@darken.eu) on 31.07.2018.
 */
@MainActivityComponent.Scope
class Navigator @Inject constructor(
        @ApplicationContext val context: Context
) {

    var mainActivity: MainActivity? = null

    fun goToLegacyVersion() {
        val pkg = "eu.thedarken.wl"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$pkg"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToManagement() {
        switchFragment(ManagerFragment::class.java, false)
    }

    fun goToOnboarding() {
        switchFragment(OnboardingFragment::class.java, false)
    }

    fun goToSettings() {
        switchFragment(SettingsFragment::class.java, true)
    }

    private fun switchFragment(fragmentClazz: Class<out androidx.fragment.app.Fragment>, backstack: Boolean) {
        if (mainActivity == null) return

        val fm = mainActivity!!.supportFragmentManager
        val current: androidx.fragment.app.Fragment? = fm.findFragmentById(R.id.content_frame)
        if (fragmentClazz.isInstance(current)) {
            Timber.d("Fragment already added: %s", fragmentClazz)
            return
        }
        val newFragment = androidx.fragment.app.Fragment.instantiate(mainActivity, fragmentClazz.name)
        val trans = fm.beginTransaction()
        trans.replace(R.id.content_frame, newFragment)
        if (backstack) trans.addToBackStack(null)
        trans.commitAllowingStateLoss()
    }
}